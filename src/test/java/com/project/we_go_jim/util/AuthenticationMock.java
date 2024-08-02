package com.project.we_go_jim.util;

import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;
import com.project.we_go_jim.model.enums.Role;

import static com.project.we_go_jim.service.AuthServiceUnitTest.USER_ENTITY;

public class AuthenticationMock {

    public static RegisterRequestDTO registerRequestDTO() {
        return RegisterRequestDTO.builder()
                .firstname(USER_ENTITY.getFirstName())
                .lastname(USER_ENTITY.getLastName())
                .email(USER_ENTITY.getEmail())
                .password("123456")
                .role(Role.USER)
                .build();
    }
    
    public static AuthenticationRequestDTO authenticationRequestDTO() {
        return AuthenticationRequestDTO.builder()
                .email(USER_ENTITY.getEmail())
                .password(USER_ENTITY.getPassword())
                .build();
    }

    public static AuthenticationResponseDTO authenticationResponseDTO() {
        return AuthenticationResponseDTO.builder().accessToken("token").build();
    }
}
