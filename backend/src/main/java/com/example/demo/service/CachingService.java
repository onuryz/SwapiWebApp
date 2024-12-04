package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.SwApiResponse;
import com.example.demo.model.People;
import com.example.demo.model.Planet;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CachingService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Cacheable(value = "peopleData", key = "'allPeople'")
  public List<People> fetchAllPeople() {
    log.info("Fetching all people from API...");
    return fetchFromApi("https://swapi.dev/api/people/", People.class);
  }

  @Cacheable(value = "planetsData", key = "'allPlanets'")
  public List<Planet> fetchAllPlanets() {
    log.info("Fetching all planets from API...");
    return fetchFromApi("https://swapi.dev/api/planets/", Planet.class);
  }

  private <T> List<T> fetchFromApi(String apiUrl, Class<T> responseType) {
    List<T> allData = new ArrayList<>();
    int currentPage = 1;

    while (true) {
      try {
        String url = apiUrl + "?page=" + currentPage;
        log.info(String.format("Requesting: %s", url));

        SwApiResponse<T> response = restTemplate.getForObject(url, SwApiResponse.class);
        if (response == null || response.getResults().isEmpty())
          break;

        List<T> results = response.getResults();
        allData.addAll(results.stream().map(item -> objectMapper.convertValue(item, responseType)).toList());
        currentPage++;
      } catch (HttpClientErrorException e) {
        if (e.getStatusCode().value() == 404) {
          break;
        } else {
          throw e;
        }
      }
    }

    return allData;
  }
}
