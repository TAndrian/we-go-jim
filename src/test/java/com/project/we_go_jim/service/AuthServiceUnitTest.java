package com.project.we_go_jim.service;

import com.project.we_go_jim.config.JwtService;
import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.model.enums.Role;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.impl.AuthServiceImpl;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {
    public static final UserEntity USER_ENTITY = UserMock.userEntity();
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void given_authRequest_when_authenticate_then_return_access_token() {
        // Arrange
        String email = USER_ENTITY.getEmail();
        String password = USER_ENTITY.getPassword();
        String token = "token";

        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(USER_ENTITY));
        when(jwtService.generateToken(USER_ENTITY)).thenReturn(token);

        // Act
        AuthenticationResponseDTO expected = authService.authenticate(authRequest);

        // Assert
        assertAll(
                () -> assertNotNull(expected),
                () -> assertEquals(token, expected.getAccessToken()),
                () -> verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(email, password)),
                () -> verify(userRepository).findByEmail(email),
                () -> verify(jwtService).generateToken(USER_ENTITY)

        );
    }

    @Test
    void given_not_found_email_when_authenticate_then_return_notFound_error() {
        // Arrange
        String email = "";
        String password = USER_ENTITY.getPassword();

        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(email, password);

        // Act
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> authService.authenticate(authRequest)
        );

        // Assert
        assertAll(
                () -> assertEquals(exception.getMessage(), UserExceptionEnum.USER_NOT_FOUND.value),
                () -> verify(userRepository, times(1)).findByEmail(email),
                () -> verify(authenticationManager, times(0))
                        .authenticate(new UsernamePasswordAuthenticationToken(email, password)),
                () -> verify(jwtService, times(0)).generateToken(any())
        );
    }

    @Test
    void given_registerRequest_when_register_then_return_access_token() {
        // Arrange
        String password = "123456";
        String encodedPassword = "encodedPassword";
        String token = "token";

        RegisterRequestDTO mockRegisterRequestDTO = RegisterRequestDTO.builder()
                .firstname(USER_ENTITY.getFirstName())
                .lastname(USER_ENTITY.getLastName())
                .email(USER_ENTITY.getEmail())
                .password(password)
                .role(Role.USER)
                .build();
        
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any())).thenReturn(USER_ENTITY);
        when(jwtService.generateToken(USER_ENTITY)).thenReturn(token);

        // Act
        AuthenticationResponseDTO expected = authService.register(mockRegisterRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(expected),
                () -> assertEquals(token, expected.getAccessToken()),
                () -> verify(userRepository, times(1)).save(any()),
                () -> verify(jwtService, times(1)).generateToken(USER_ENTITY)
        );
    }
}
