package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserMock {
    public static final UUID USER_ID = UUID.randomUUID();
    public static final UUID JOHN_ID = UUID.fromString("4fab73f9-b152-4bb6-a515-ed13cef63a0c");

    public static UserEntity userEntity() {
        return UserEntity.builder()
                .id(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .bookings(Set.of(BookingMock.bookingEntity()))
                .build();
    }

    public static UserDTO userDTO() {
        return UserDTO.builder()
                .id(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .bookings(Set.of(BookingMock.bookingDTO()))
                .build();
    }

    public static CreateUserDTO createUserDTO() {
        return CreateUserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .password("123456")
                .bookings(Set.of())
                .build();
    }

    public static UserEntity createUserEntity() {
        return UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .bookings(Set.of())
                .build();
    }

    public static CreateUserDTO userBadlyDefinedDTO() {
        return CreateUserDTO.builder()
                .firstName("")
                .build();
    }

    public static UserEntity userWithoutBookingEntity() {
        return UserEntity.builder()
                .id(USER_ID)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .bookings(new HashSet<>())
                .build();
    }

    public static UserEntity userAssignedToBookingEntity() {
        Set<BookingEntity> bookingEntities = new HashSet<>();
        BookingEntity bookingEntity = BookingMock.bookingEntity();
        bookingEntity.setMaxParticipant(bookingEntity.getMaxParticipant() + 1);
        bookingEntities.add(bookingEntity);

        return UserEntity.builder()
                .id(USER_ID)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .bookings(bookingEntities)
                .build();
    }

    public static UserDTO userAssignedToBookingDTO() {
        Set<BookingDTO> bookingDTOS = new HashSet<>();
        BookingDTO bookingDTO = BookingMock.bookingDTO();
        bookingDTO.setMaxParticipant(bookingDTO.getMaxParticipant() + 1);
        bookingDTOS.add(bookingDTO);

        return UserDTO.builder()
                .id(USER_ID)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .bookings(bookingDTOS)
                .build();
    }
}
