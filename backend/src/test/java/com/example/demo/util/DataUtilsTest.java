package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.People;
import com.example.demo.model.Planet;

public class DataUtilsTest {

    private List<People> mockPeopleList;
    private List<Planet> mockPlanetsList;

    @BeforeEach
    public void setUp() {
        mockPeopleList = Arrays.asList(
            new People("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male", null, null, null, null, null, null, null, null),
            new People("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female", null, null, null, null, null, null, null, null),
            new People("Darth Vader", "202", "136", "none", "white", "yellow", "41.9BBY", "male", null, null, null, null, null, null, null, null)
        );

        mockPlanetsList = Arrays.asList(
            new Planet("Tatooine", "23", "304", "10465", "arid", null,  null, null, null, null, null, null, null, null),
            new Planet("Alderaan", "24", "364", "12500", "temperate", null,  null, null, null, null, null, null, null, null),
            new Planet("Yavin IV", "24", "4818", "10200", "temperate, tropical", null, null, null, null, null, null, null, null, null)
        );
    }

    @Test
    public void testSortData() {
        //People
        List<People> sortedPeopleByNameAsc = DataUtils.sortData(mockPeopleList, "name", "asc");
        assertEquals("Darth Vader", sortedPeopleByNameAsc.get(0).getName());
        assertEquals("Leia Organa", sortedPeopleByNameAsc.get(1).getName());
        assertEquals("Luke Skywalker", sortedPeopleByNameAsc.get(2).getName());

        List<People> sortedPeopleByNameDesc = DataUtils.sortData(mockPeopleList, "name", "desc");
        assertEquals("Luke Skywalker", sortedPeopleByNameDesc.get(0).getName());
        assertEquals("Leia Organa", sortedPeopleByNameDesc.get(1).getName());
        assertEquals("Darth Vader", sortedPeopleByNameDesc.get(2).getName());


        //Planet
        List<Planet> sortedPlanetsByNameAsc = DataUtils.sortData(mockPlanetsList, "name", "asc");
        assertEquals("Alderaan", sortedPlanetsByNameAsc.get(0).getName());
        assertEquals("Tatooine", sortedPlanetsByNameAsc.get(1).getName());
        assertEquals("Yavin IV", sortedPlanetsByNameAsc.get(2).getName());

        List<Planet> sortedPlanetsByNameDesc = DataUtils.sortData(mockPlanetsList, "name", "desc");
        assertEquals("Yavin IV", sortedPlanetsByNameDesc.get(0).getName());
        assertEquals("Tatooine", sortedPlanetsByNameDesc.get(1).getName());
        assertEquals("Alderaan", sortedPlanetsByNameDesc.get(2).getName());
    }

    @Test
    public void testFilterData() {
        //People
        List<People> filteredPeopleByName = DataUtils.filterData(mockPeopleList, "name", "Leia");
        assertEquals(1, filteredPeopleByName.size());
        assertEquals("Leia Organa", filteredPeopleByName.get(0).getName());

        List<People> filteredPeopleByGender = DataUtils.filterData(mockPeopleList, "gender", "male");
        assertEquals(3, filteredPeopleByGender.size());

        List<People> filteredPeopleByGender2 = DataUtils.filterData(mockPeopleList, "gender", "female");
        assertEquals(1, filteredPeopleByGender2.size());

        List<People> filteredPeopleWithNoMatch = DataUtils.filterData(mockPeopleList, "name", "Han");
        assertEquals(0, filteredPeopleWithNoMatch.size());


        //Planet
        List<Planet> filteredPlanetsByName = DataUtils.filterData(mockPlanetsList, "name", "Tatoo");
        assertEquals(1, filteredPlanetsByName.size());
        assertEquals("Tatooine", filteredPlanetsByName.get(0).getName());

        List<Planet> filteredPlanetsByGender = DataUtils.filterData(mockPlanetsList, "climate", "temperate");
        assertEquals(2, filteredPlanetsByGender.size());

        List<Planet> filteredPlanetsWithNoMatch = DataUtils.filterData(mockPlanetsList, "name", "Endor");
        assertEquals(0, filteredPlanetsWithNoMatch.size());
    }

    @Test
    public void testPaginateData() {
        //People
        ApiResponse<People> firstPeoplePage = DataUtils.paginateData(mockPeopleList, 1, 2);
        assertEquals(1, firstPeoplePage.getCurrentPage());
        assertEquals(2, firstPeoplePage.getNumberOfPages());
        assertEquals(2, firstPeoplePage.getData().size());
        assertEquals("Luke Skywalker", firstPeoplePage.getData().get(0).getName());

        ApiResponse<People> secondPeoplePage = DataUtils.paginateData(mockPeopleList, 2, 2);
        assertEquals(2, secondPeoplePage.getCurrentPage());
        assertEquals(2, secondPeoplePage.getNumberOfPages());
        assertEquals(1, secondPeoplePage.getData().size());
        assertEquals("Darth Vader", secondPeoplePage.getData().get(0).getName());

        ApiResponse<People> outOfBoundsPeoplePage = DataUtils.paginateData(mockPeopleList, 3, 2);
        assertEquals(3, outOfBoundsPeoplePage.getCurrentPage());
        assertEquals(2, outOfBoundsPeoplePage.getNumberOfPages());
        assertTrue(outOfBoundsPeoplePage.getData().isEmpty());

        
        //Planet
        ApiResponse<Planet> firstPlanetsPage = DataUtils.paginateData(mockPlanetsList, 1, 2);
        assertEquals(1, firstPlanetsPage.getCurrentPage());
        assertEquals(2, firstPlanetsPage.getNumberOfPages());
        assertEquals(2, firstPlanetsPage.getData().size());
        assertEquals("Tatooine", firstPlanetsPage.getData().get(0).getName());

        ApiResponse<Planet> secondPlanetsPage = DataUtils.paginateData(mockPlanetsList, 2, 2);
        assertEquals(2, secondPlanetsPage.getCurrentPage());
        assertEquals(2, secondPlanetsPage.getNumberOfPages());
        assertEquals(1, secondPlanetsPage.getData().size());
        assertEquals("Yavin IV", secondPlanetsPage.getData().get(0).getName());

        ApiResponse<Planet> outOfBoundsPlanetsPage = DataUtils.paginateData(mockPlanetsList, 3, 2);
        assertEquals(3, outOfBoundsPlanetsPage.getCurrentPage());
        assertEquals(2, outOfBoundsPlanetsPage.getNumberOfPages());
        assertTrue(outOfBoundsPlanetsPage.getData().isEmpty());
    }

    @Test
    public void testEmptyDataHandling() {
        //People
        List<People> emptyPeopleList = new ArrayList<>();
        List<Planet> emptyPlanetList = new ArrayList<>();

        List<People> sortedEmptyPeople = DataUtils.sortData(emptyPeopleList, "name", "asc");
        assertTrue(sortedEmptyPeople.isEmpty());

        List<People> filteredEmptyPeople = DataUtils.filterData(emptyPeopleList, "name", "Luke");
        assertTrue(filteredEmptyPeople.isEmpty());

        ApiResponse<People> paginatedEmptyPeople = DataUtils.paginateData(emptyPeopleList, 1, 10);
        assertEquals(1, paginatedEmptyPeople.getCurrentPage());
        assertEquals(0, paginatedEmptyPeople.getNumberOfPages());
        assertTrue(paginatedEmptyPeople.getData().isEmpty());

        
        //Planet
        List<Planet> sortedEmptyPlanet = DataUtils.sortData(emptyPlanetList, "name", "asc");
        assertTrue(sortedEmptyPlanet.isEmpty());

        List<Planet> filteredEmptyPlanet = DataUtils.filterData(emptyPlanetList, "name", "Tatoo");
        assertTrue(filteredEmptyPlanet.isEmpty());

        ApiResponse<Planet> paginatedEmptyPlanet = DataUtils.paginateData(emptyPlanetList, 1, 10);
        assertEquals(1, paginatedEmptyPlanet.getCurrentPage());
        assertEquals(0, paginatedEmptyPlanet.getNumberOfPages());
        assertTrue(paginatedEmptyPlanet.getData().isEmpty());
    }
}
