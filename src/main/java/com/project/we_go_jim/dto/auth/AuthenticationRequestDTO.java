package com.project.we_go_jim.dto.auth;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
