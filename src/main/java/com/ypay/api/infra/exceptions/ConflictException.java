package com.ypay.api.infra.exceptions;

public class ConflictException extends Exception {
    public ConflictException(String field) {
        super("Já possui esse " + field);
    }
}
