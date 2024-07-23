package com.project.we_go_jim.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingExceptionEnum {
    BOOKING_EXCEPTION_CODE("com.project.we-go-jim.booking.resource"),
    BOOKING_BAD_REQUEST("Booking bad request"),
    BOOKING_MAX_PARTICIPANT_OVER_TEN("There are already ten users assigned to the current schedule"),
    BOOKING_NOT_FOUND("Booking not found"),
    BOOKING_ALREADY_BOOKED_FOR_CURRENT_USER("The current user has already booked this shcedule."),
    CREATE_BOOKING_BAD_REQUEST("CreateBooking bad request");
    public final String value;
}
