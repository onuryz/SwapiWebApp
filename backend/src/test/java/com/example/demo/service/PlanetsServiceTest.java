package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.Planet;

class PlanetsServiceTest {

    @Mock
    private CachingService cachingService;

    @InjectMocks
    private PlanetsService planetsService;

    private List<Planet> mockPlanetsList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockPlanetsList = Arrays.asList(
            new Planet("Tatooine", "23", "304", "10465", "arid", null,  null, null, null, null, null, null, null, null),
            new Planet("Alderaan", "24", "364", "12500", "temperate", null,  null, null, null, null, null, null, null, null),
            new Planet("Yavin IV", "24", "4818", "10200", "temperate, tropical", null, null, null, null, null, null, null, null, null)
        );
    }

    @Test
    void testGetPlanets_noFilterNoSort() {
        when(cachingService.fetchAllPlanets()).thenReturn(mockPlanetsList);

        ApiResponse<Planet> response = planetsService.getPlanets(1, 2, null, null, null, null);

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(2, response.getNumberOfPages());
        assertEquals(2, response.getData().size());
        assertEquals("Tatooine", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPlanets();
    }

    @Test
    void testGetPlanets_withFilter() {
        when(cachingService.fetchAllPlanets()).thenReturn(mockPlanetsList);

        ApiResponse<Planet> response = planetsService.getPlanets(1, 2, "Ald", "name", null, null);

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(1, response.getData().size());
        assertEquals("Alderaan", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPlanets();
    }

    @Test
    void testGetPlanets_withSort() {
        when(cachingService.fetchAllPlanets()).thenReturn(mockPlanetsList);

        ApiResponse<Planet> response = planetsService.getPlanets(1, 3, null, null, "name", "desc");

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(3, response.getData().size());
        assertEquals("Yavin IV", response.getData().get(0).getName());
        assertEquals("Tatooine", response.getData().get(1).getName());
        assertEquals("Alderaan", response.getData().get(2).getName());

        verify(cachingService, times(1)).fetchAllPlanets();
    }

    @Test
    void testGetPlanets_withPagination() {
        when(cachingService.fetchAllPlanets()).thenReturn(mockPlanetsList);

        ApiResponse<Planet> response = planetsService.getPlanets(2, 2, null, null, null, null);

        assertNotNull(response);
        assertEquals(2, response.getCurrentPage());
        assertEquals(2, response.getNumberOfPages());
        assertEquals(1, response.getData().size());
        assertEquals("Yavin IV", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPlanets();
    }

    @Test
    void testGetPlanets_withFilterAndSort() {
        when(cachingService.fetchAllPlanets()).thenReturn(mockPlanetsList);

        ApiResponse<Planet> response = planetsService.getPlanets(1, 3, "temperate", "climate", "name", "asc");

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(2, response.getData().size());
        assertEquals("Alderaan", response.getData().get(0).getName());
        assertEquals("Yavin IV", response.getData().get(1).getName());

        verify(cachingService, times(1)).fetchAllPlanets();
    }
}
