package com.ypay.api.infra.exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Credenciais inválidas");
    }
}
