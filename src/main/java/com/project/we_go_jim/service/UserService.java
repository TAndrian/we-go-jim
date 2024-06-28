package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Retrieve all users.
     *
     * @return all users.
     */
    List<UserDTO> getUsers();

    /**
     * Create user.
     *
     * @param userToCreate user to create.
     */
    void createUser(CreateUserDTO userToCreate);

    /**
     * Retrieve user based on the giver userId.
     *
     * @param userId userId.
     * @return corresponding user to the userId.
     */
    UserDTO getUserById(UUID userId);

    /**
     * Add booking to user if the slot is available based on the maxParticipant.
     * If the booking does not exist, it will be created then assigned to the user.
     *
     * @param userId         userId.
     * @param startTime      startTime.
     * @param endTime        endTime.
     * @param maxParticipant maximum participants.
     * @return booking assigned to user.
     */
    UserDTO addBookingToUser(UUID userId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             Integer maxParticipant);
}
