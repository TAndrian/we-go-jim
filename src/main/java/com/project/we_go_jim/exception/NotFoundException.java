package com.project.we_go_jim.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
