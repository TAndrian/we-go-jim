package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getUsers();

    void createUser(CreateUserDTO userToCreate);

    UserDTO getUserById(UUID userId);
}
