package com.ypay.api.domain.user;

public enum UserType {
    FISICA("Fisica"),
    JURIDICA("Juridica");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String valueOf() {
        return userType;
    }
}
