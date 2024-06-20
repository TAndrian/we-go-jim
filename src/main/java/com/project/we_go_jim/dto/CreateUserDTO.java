package com.project.we_go_jim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CreateUserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<BookingDTO> bookings;
}
