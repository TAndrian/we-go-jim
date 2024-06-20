package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.model.UserEntity;

import java.util.Set;
import java.util.UUID;

public class UserMock {
    public static final UUID USER_ID = UUID.randomUUID();

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

    public static UserDTO createdUserDTO() {
        return UserDTO.builder()
                .id(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
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
}
