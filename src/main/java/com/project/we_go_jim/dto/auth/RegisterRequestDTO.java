package com.project.we_go_jim.dto.auth;

import com.project.we_go_jim.model.enums.Role;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RegisterRequestDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
