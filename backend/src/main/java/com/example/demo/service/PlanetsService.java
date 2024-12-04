package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.Planet;
import com.example.demo.util.DataUtils;

@Service
public class PlanetsService {

  @Autowired
  private CachingService cachingService;

  public ApiResponse<Planet> getPlanets(int page, int size, String filterValue, String filterField, String sort, String order) {
    List<Planet> allData = cachingService.fetchAllPlanets();

    if (filterValue != null) {
      allData = DataUtils.filterData(allData, filterField, filterValue);
    }

    if (sort != null) {
      allData = DataUtils.sortData(allData, sort, order);
    }

    return DataUtils.paginateData(allData, page, size);
  }
}
