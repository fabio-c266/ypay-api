package com.ypay.api.infra.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String entityName) {
        super(entityName + " não encontrada");
    }
}
