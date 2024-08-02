package com.project.we_go_jim.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthenticationResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
}
