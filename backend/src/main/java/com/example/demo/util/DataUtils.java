package com.example.demo.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.ApiResponse;

public class DataUtils {

  public static <T> List<T> sortData(List<T> data, String sortField, String sortOrder) {
    List<T> sordtedData = new ArrayList<>(data);
    sordtedData.sort((o1, o2) -> {
      try {
        String value1 = getFieldValue(o1, sortField);
        String value2 = getFieldValue(o2, sortField);

        int comparisonResult = value1.compareTo(value2);

        if ("desc".equalsIgnoreCase(sortOrder)) {
          comparisonResult = -comparisonResult;
        }

        return comparisonResult;
      } catch (Exception e) {
        return 0;
      }
    });
    return sordtedData;
  }

  public static <T> List<T> filterData(List<T> data, String filterField, String filterValue) {
    return data.stream()
        .filter(item -> {
          try {
            String fieldValue = getFieldValue(item, filterField);
            return fieldValue.toLowerCase().contains(filterValue.toLowerCase());
          } catch (Exception e) {
            return false;
          }
        })
        .toList();
  }

  public static <T> ApiResponse<T> paginateData(List<T> data, int pageNumber, int pageSize) {
    int fromIndex = (pageNumber - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, data.size());
    int totalPages = (int) Math.ceil((double) data.size() / pageSize);

    if (fromIndex >= data.size()) {
      return new ApiResponse<T>(pageNumber, totalPages, List.of());
    }

    return new ApiResponse<T>(pageNumber, totalPages, data.subList(fromIndex, toIndex));
  }

  private static <T> String getFieldValue(T object, String fieldName) throws Exception {
    String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    Method method = object.getClass().getMethod(methodName);
    Object value = method.invoke(object);
    return value != null ? value.toString() : "";
  }
}
