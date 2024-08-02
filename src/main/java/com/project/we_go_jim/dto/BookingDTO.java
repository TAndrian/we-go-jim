package com.project.we_go_jim.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookingDTO {
    private UUID id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxParticipant;

    private Set<UserDTO> users;
}
// Décalage horaire
