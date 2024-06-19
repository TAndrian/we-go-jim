package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();

    UserDTO createUser(UserDTO userToCreate);
}
