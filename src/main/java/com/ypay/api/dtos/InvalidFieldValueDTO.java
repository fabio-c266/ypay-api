package com.ypay.api.dtos;

public record InvalidFieldValueDTO(
        String field,
        String message
) {
}
