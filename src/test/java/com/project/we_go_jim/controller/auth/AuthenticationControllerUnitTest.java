package com.project.we_go_jim.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.config.JwtAuthenticationFilter;
import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.service.AuthService;
import com.project.we_go_jim.util.AuthenticationMock;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerUnitTest {

    private static final UserEntity USER_ENTITY = UserMock.userEntity();

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void given_authRequest_when_authenticate_then_return_access_token() throws Exception {
        // ARRANGE
        final String url = "/api/v1/auth/authenticate";
        String token = "token";

        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                .email(USER_ENTITY.getEmail())
                .password(USER_ENTITY.getPassword())
                .build();

        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().accessToken(token).build();

        String requestBody = objectMapper.writeValueAsString(requestDTO);
        String responseBody = objectMapper.writeValueAsString(responseDTO);

        when(authService.authenticate(requestDTO))
                .thenReturn(responseDTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(authService, times(1)).authenticate(requestDTO);
    }

    @Test
    void given_register_request_when_register_then_return_access_token() throws Exception {
        // ARRANGE
        final String url = "/api/v1/auth/register";

        RegisterRequestDTO mockRegisterRequestDTO = AuthenticationMock.registerRequestDTO();

        AuthenticationResponseDTO mockResponseDTO = AuthenticationMock.authenticationResponseDTO();

        String requestBody = objectMapper.writeValueAsString(mockRegisterRequestDTO);
        String responseBody = objectMapper.writeValueAsString(mockResponseDTO);

        when(authService.register(mockRegisterRequestDTO))
                .thenReturn(mockResponseDTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(authService, times(1)).register(mockRegisterRequestDTO);
    }
}
