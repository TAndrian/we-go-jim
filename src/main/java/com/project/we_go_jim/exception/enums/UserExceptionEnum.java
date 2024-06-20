package com.project.we_go_jim.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExceptionEnum {
    USER_EXCEPTION_CODE("com.project.we-go-jim.user.resource"),
    USER_NOT_FOUND("User not found"),
    USER_BAD_REQUEST("User bad request"),
    USER_BAD_REQUEST_EMAIL("User's email badly defined"),
    USER_BAD_REQUEST_USERNAME("User's firstname or lastname badly defined.");
    public final String value;
}
