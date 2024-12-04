package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.Planet;
import com.example.demo.service.PlanetsService;

@RestController
public class PlanetsController {

  private final PlanetsService planetsService;

  public PlanetsController(PlanetsService planetsService) {
    this.planetsService = planetsService;
  }

  @GetMapping("/planets")
  public ResponseEntity<ApiResponse<Planet>> getPlanets(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int size,
      @RequestParam(required = false) String filterValue,
      @RequestParam(defaultValue = "name") String filterField,
      @RequestParam(required = false) String sort,
      @RequestParam(defaultValue = "asc") String order) {
    return ResponseEntity.ok(planetsService.getPlanets(page, size, filterValue, filterField, sort, order));
  }
}