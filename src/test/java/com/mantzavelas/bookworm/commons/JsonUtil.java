package com.mantzavelas.bookworm.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public enum JsonUtil {

    ;

    public static String toJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static String listToJsonString(final List<?> object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
