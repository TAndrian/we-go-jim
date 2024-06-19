package com.project.we_go_jim.controller.resource;

import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.project.we_go_jim.controller.ResourcesPath.API_USER;
import static com.project.we_go_jim.controller.ResourcesPath.API_USERS;

@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class UserResourceController {

    private UserService userService;

    @PostMapping(API_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserDTO userToCreate) {
        return userService.createUser(userToCreate);
    }

    @GetMapping(API_USERS)
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }
}
