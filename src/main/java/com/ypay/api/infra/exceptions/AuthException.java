package com.ypay.api.infra.exceptions;

public class AuthException extends Exception {
    public AuthException() {
        super("Ocorreu um erro durante a autenticação");
    }
}
