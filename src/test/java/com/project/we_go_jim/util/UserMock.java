package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.model.UserEntity;

import java.util.HashSet;
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
                .bookings(new HashSet<>())
                .build();
    }

    public static UserEntity janeSmithEntity() {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .bookings(new HashSet<>())
                .build();
    }

    public static UserDTO janeSmithDTO() {
        return UserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .build();
    }

    public static UserDTO userDTO() {
        return UserDTO.builder()
                .id(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .build();
    }

    public static CreateUserDTO createUserDTO() {
        return CreateUserDTO.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .password("123456")
                .build();
    }

    public static UserEntity createUserEntity() {
        return UserEntity.builder()
                .firstName(createUserDTO().getFirstName())
                .lastName(createUserDTO().getLastName())
                .email(createUserDTO().getEmail())
                .build();
    }

    public static CreateUserDTO userBadlyDefinedDTO() {
        return CreateUserDTO.builder()
                .firstName("")
                .build();
    }
}
