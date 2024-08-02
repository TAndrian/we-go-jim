package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.auth.AuthenticationRequestDTO;
import com.project.we_go_jim.dto.auth.AuthenticationResponseDTO;
import com.project.we_go_jim.dto.auth.RegisterRequestDTO;

public interface AuthService {
    /**
     * Authenticate user from authRequest data.
     *
     * @param authRequest authentication request.
     * @return access token.
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authRequest);

    /**
     * Register user with the registerRequest data.
     *
     * @param registerRequest register request.
     * @return access token to confirm that the user has been register successfully.
     */
    AuthenticationResponseDTO register(RegisterRequestDTO registerRequest);

}
