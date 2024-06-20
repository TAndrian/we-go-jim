package com.project.we_go_jim.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExceptionEnum {
    USER_EXCEPTION_CODE("com.project.we-go-jim.user.resource"),
    USER_NOT_FOUND("User not found"),
    USER_BAD_REQUEST("User bad request");
    public final String value;
}
