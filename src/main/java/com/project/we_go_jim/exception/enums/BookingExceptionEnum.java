package com.project.we_go_jim.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingExceptionEnum {
    BOOKING_EXCEPTION_CODE("com.project.we-go-jim.booking.resource"),
    BOOKING_BAD_REQUEST("Booking bad request"),
    BOOKING_NOT_FOUND("Booking not found");
    public final String value;
}
