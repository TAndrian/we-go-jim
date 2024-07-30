package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.config.JwtService;
import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        var userEntity = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(
                () -> {
                    log.info("User not found with the user email: {}", authRequest.getEmail());
                    return new NotFoundException(
                            UserExceptionEnum.USER_NOT_FOUND.value,
                            UserExceptionEnum.USER_EXCEPTION_CODE.value
                    );
                }
        );

        String token = jwtService.generateToken(userEntity);


        return AuthenticationResponseDTO.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequest) {
        var userEntity = userRepository.save(
                UserEntity.builder()
                        .firstName(registerRequest.getFirstname())
                        .lastName(registerRequest.getLastname())
                        .email(registerRequest.getEmail())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .role(registerRequest.getRole())
                        .build()
        );

        String jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponseDTO.builder().accessToken(jwtToken).build();
    }
}
