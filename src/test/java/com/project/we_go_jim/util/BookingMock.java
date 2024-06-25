package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.model.BookingEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingMock {
    public static final UUID BOOKING_ID = UUID.randomUUID();
    public static final LocalDateTime START_TIME = LocalDateTime.parse("2024-06-19T12:00:00");
    public static final LocalDateTime END_TIME = LocalDateTime.parse("2024-06-19T12:30:00");

    public static BookingEntity bookingEntity() {
        return BookingEntity.builder()
                .id(BOOKING_ID)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .maxParticipant(3)
                .build();
    }

    public static BookingDTO bookingDTO() {
        return BookingDTO.builder()
                .id(BOOKING_ID)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .maxParticipant(3)
                .build();
    }
}
