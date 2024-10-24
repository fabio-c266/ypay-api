package com.ypay.api.infra.exceptions;

public class DTOEmptyException extends Exception {
    public DTOEmptyException() {
        super("Body vazio");
    }
}
