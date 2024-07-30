package com.project.we_go_jim.controller.auth;

import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor()
@RequestMapping(
        path = "/api/v1/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @PostMapping("/authenticate")
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO authRequestDTO) {
        return AuthenticationResponseDTO.builder().build();
    }

    @PostMapping("/register")
    public AuthenticationResponseDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return AuthenticationResponseDTO.builder().build();
    }
}
