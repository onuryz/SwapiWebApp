package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.People;
import com.example.demo.model.Planet;
import com.example.demo.model.SwApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CachingServiceTest {
  
  private final DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

  @InjectMocks
  private CachingService cachingService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFetchAllPeople() throws Exception {
    mockPeopleEndpoint();

    List<People> peopleFirstCall = cachingService.fetchAllPeople();
    assertNotNull(peopleFirstCall);
    assertEquals(18, peopleFirstCall.size());

    verify(restTemplate, times(3)).getForObject(anyString(), eq(SwApiResponse.class));

    //TODO: test caching
    // reset(restTemplate);
    // List<People> peopleSecondCall = cachingService.fetchAllPeople();
    // assertNotNull(peopleSecondCall);
    // assertEquals(peopleFirstCall.size(), peopleSecondCall.size());
    // verify(restTemplate, times(0)).getForObject(anyString(), eq(SwApiResponse.class));
  }

  @Test
  void testFetchAllPlanets() throws Exception {
    mockPlanetsEndpoint();

    List<Planet> planetsFirstCall = cachingService.fetchAllPlanets();
    assertNotNull(planetsFirstCall);
    assertEquals(12, planetsFirstCall.size());
    verify(restTemplate, times(3)).getForObject(anyString(), eq(SwApiResponse.class));

    //TODO: test caching
    // reset(restTemplate);
    // List<Planet> planetsSecondCall = cachingService.fetchAllPlanets();
    // assertNotNull(planetsSecondCall);
    // assertEquals(planetsFirstCall.size(), planetsSecondCall.size());
    // verify(restTemplate, times(0)).getForObject(anyString(), eq(SwApiResponse.class));
  }
  
  private String loadJsonFromResource(String resourcePath) throws IOException {
    Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
    File file = resource.getFile();
    return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
  }

  private void mockPeopleEndpoint() throws Exception {
    String mockPeoplePage1Json = loadJsonFromResource("mockdata/people_page1.json");
    String mockPeoplePage2Json = loadJsonFromResource("mockdata/people_page2.json");

    SwApiResponse<People> firstPageResponse = new ObjectMapper().readValue(mockPeoplePage1Json, new TypeReference<SwApiResponse<People>>() {});
    SwApiResponse<People> secondPageResponse = new ObjectMapper().readValue(mockPeoplePage2Json, new TypeReference<SwApiResponse<People>>() {});

    when(restTemplate.getForObject("https://swapi.dev/api/people/?page=1", SwApiResponse.class)).thenReturn(firstPageResponse);
    when(restTemplate.getForObject("https://swapi.dev/api/people/?page=2", SwApiResponse.class)).thenReturn(secondPageResponse);
    when(restTemplate.getForObject("https://swapi.dev/api/people/?page=3", SwApiResponse.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
  }

  private void mockPlanetsEndpoint() throws Exception {
    String mockPlanetPage1Json = loadJsonFromResource("mockdata/planets_page1.json");
    String mockPlanetPage2Json = loadJsonFromResource("mockdata/planets_page2.json");

    SwApiResponse<Planet> firstPageResponse = new ObjectMapper().readValue(mockPlanetPage1Json, new TypeReference<SwApiResponse<Planet>>() {});
    SwApiResponse<Planet> secondPageResponse = new ObjectMapper().readValue(mockPlanetPage2Json, new TypeReference<SwApiResponse<Planet>>() {});

    when(restTemplate.getForObject("https://swapi.dev/api/planets/?page=1", SwApiResponse.class)).thenReturn(firstPageResponse);
    when(restTemplate.getForObject("https://swapi.dev/api/planets/?page=2", SwApiResponse.class)).thenReturn(secondPageResponse);
    when(restTemplate.getForObject("https://swapi.dev/api/planets/?page=3", SwApiResponse.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
  }
}
