package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse<T> {

    @JsonProperty("currentPage")
    private int  currentPage;

    @JsonProperty("numberOfPages")
    private int  numberOfPages;
    
    @JsonProperty("data")
    private List<T> data;

}

