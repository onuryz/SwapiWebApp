package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.People;
import com.example.demo.service.PeopleService;

@RestController
public class PeopleController {

  private final PeopleService peopleService;

  public PeopleController(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @GetMapping("/people")
  public ResponseEntity<ApiResponse<People>> getPeople(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int size,
      @RequestParam(required = false) String filterValue,
      @RequestParam(defaultValue = "name") String filterField,
      @RequestParam(required = false) String sort,
      @RequestParam(defaultValue = "asc") String order) {
    return ResponseEntity.ok(peopleService.getPeople(page, size, filterValue, filterField, sort, order));
  }
}
