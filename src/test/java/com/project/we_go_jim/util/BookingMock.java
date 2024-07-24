package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BookingMock {
    public static final UUID BOOKING_ID = UUID.randomUUID();
    public static final LocalDateTime START_TIME = LocalDateTime.parse("2024-06-19T12:00:00");
    public static final LocalDateTime END_TIME = LocalDateTime.parse("2024-06-19T12:30:00");

    public static BookingEntity bookingEntity() {
        Set<UserEntity> users = new HashSet<>();
        users.add(UserMock.userEntity());
        return BookingEntity.builder()
                .id(BOOKING_ID)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .maxParticipant(3)
                .users(users)
                .build();
    }

    public static BookingDTO bookingDTO() {
        Set<UserDTO> users = new HashSet<>();
        users.add(UserMock.userDTO());
        return BookingDTO.builder()
                .id(BOOKING_ID)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .maxParticipant(3)
                .users(users)
                .build();
    }

    public static UserBookingHistoryDTO userBookingHistoryDTO() {
        return UserBookingHistoryDTO.builder()
                .id(BOOKING_ID)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();
    }

    public static CreateBookingDTO createBookingDTO() {
        return CreateBookingDTO.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
    }

    public static BookingEntity createdBookingEntity() {
        UserEntity userEntity = UserMock.userEntity();
        Set<UserEntity> users = new HashSet<>();
        users.add(userEntity);

        LocalDateTime startTime = createBookingDTO().getStartTime();
        LocalDateTime endTime = createBookingDTO().getEndTime();
        return BookingEntity.builder()
                .id(UUID.randomUUID())
                .startTime(startTime)
                .endTime(endTime)
                .maxParticipant(1)
                .users(users)
                .build();
    }

    public static BookingDTO createdBookingDTO() {
        UserDTO userDTO = UserMock.userDTO();
        Set<UserDTO> users = new HashSet<>();
        users.add(userDTO);

        LocalDateTime startTime = createBookingDTO().getStartTime();
        LocalDateTime endTime = createBookingDTO().getEndTime();
        return BookingDTO.builder()
                .id(UUID.randomUUID())
                .startTime(startTime)
                .endTime(endTime)
                .maxParticipant(1)
                .users(users)
                .build();
    }
}
