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
import com.example.demo.model.People;

class PeopleServiceTest {

    @Mock
    private CachingService cachingService;

    @InjectMocks
    private PeopleService peopleService;

    private List<People> mockPeopleList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockPeopleList = Arrays.asList(
            new People("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male", null, null, null, null, null, null, null, null),
            new People("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female", null, null, null, null, null, null, null, null),
            new People("Darth Vader", "202", "136", "none", "white", "yellow", "41.9BBY", "male", null, null, null, null, null, null, null, null)
        );
    }

    @Test
    void testGetPeople_noFilterNoSort() {
        when(cachingService.fetchAllPeople()).thenReturn(mockPeopleList);

        ApiResponse<People> response = peopleService.getPeople(1, 2, null, null, null, null);

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(2, response.getNumberOfPages());
        assertEquals(2, response.getData().size());
        assertEquals("Luke Skywalker", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPeople();
    }

    @Test
    void testGetPeople_withFilter() {
        when(cachingService.fetchAllPeople()).thenReturn(mockPeopleList);

        ApiResponse<People> response = peopleService.getPeople(1, 2, "Leia", "name", null, null);

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(1, response.getData().size());
        assertEquals("Leia Organa", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPeople();
    }

    @Test
    void testGetPeople_withSort() {
        when(cachingService.fetchAllPeople()).thenReturn(mockPeopleList);

        ApiResponse<People> response = peopleService.getPeople(1, 3, null, null, "name", "desc");

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(3, response.getData().size());
        assertEquals("Luke Skywalker", response.getData().get(0).getName());
        assertEquals("Leia Organa", response.getData().get(1).getName());
        assertEquals("Darth Vader", response.getData().get(2).getName());

        verify(cachingService, times(1)).fetchAllPeople();
    }

    @Test
    void testGetPeople_withPagination() {
        when(cachingService.fetchAllPeople()).thenReturn(mockPeopleList);

        ApiResponse<People> response = peopleService.getPeople(2, 2, null, null, null, null);

        assertNotNull(response);
        assertEquals(2, response.getCurrentPage());
        assertEquals(2, response.getNumberOfPages());
        assertEquals(1, response.getData().size());
        assertEquals("Darth Vader", response.getData().get(0).getName());

        verify(cachingService, times(1)).fetchAllPeople();
    }

    @Test
    void testGetPeople_withFilterAndSort() {
        when(cachingService.fetchAllPeople()).thenReturn(mockPeopleList);

        ApiResponse<People> response = peopleService.getPeople(1, 3, "19BBY", "birthYear", "name", "asc");

        assertNotNull(response);
        assertEquals(1, response.getCurrentPage());
        assertEquals(1, response.getNumberOfPages());
        assertEquals(2, response.getData().size());
        assertEquals("Leia Organa", response.getData().get(0).getName());
        assertEquals("Luke Skywalker", response.getData().get(1).getName());

        verify(cachingService, times(1)).fetchAllPeople();
    }
}
