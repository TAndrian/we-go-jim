package com.project.we_go_jim.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final String code;

    public BadRequestException(String message, String code) {
        super(message);
        this.code = code;
    }
}
