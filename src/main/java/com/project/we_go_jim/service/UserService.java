package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;

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
}
