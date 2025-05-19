package com.doankietdev.identityservice.application.mapper.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.doankietdev.identityservice.shared.constants.SortConstants;

public abstract class AbstractSortFieldMapper implements SortFieldMapper {
  protected abstract Map<String, String> getFieldMap();

  @Override
  public String map(String sortFields) {
    Map<String, String> fieldMap = getFieldMap();

    List<String> mappedSortFields = new ArrayList<>();
    String[] sortFieldArray = sortFields.split(",");
    for (int i = 0; i < sortFieldArray.length; i++) {
      String field = sortFieldArray[i].trim();
      String mappedField = fieldMap.getOrDefault(field, field);
      mappedSortFields.add(mappedField);
    }

    return String.join(SortConstants.SORT_FIELD_DELIMITER, mappedSortFields);
  }
}
