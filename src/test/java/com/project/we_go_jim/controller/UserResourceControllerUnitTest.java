package com.project.we_go_jim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.controller.resource.UserResourceController;
import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.service.UserService;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_USER;
import static com.project.we_go_jim.controller.ResourcesPath.API_USERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserResourceController.class)
class UserResourceControllerUnitTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_get_all_users_then_return_users() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USERS);
        List<UserDTO> mockResponse = List.of(UserMock.userDTO());

        when(userService.getUsers()).thenReturn(mockResponse);

        String response = objectMapper.writeValueAsString(mockResponse);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        // ASSERT
        verify(userService, times(1)).getUsers();
    }

    @Test
    void given_no_users_when_get_all_users_then_return_empty_collection() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USERS);
        String response = objectMapper.writeValueAsString(List.of());

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        // ASSERT
        verify(userService, times(1)).getUsers();
    }

    @Test
    void given_userId_when_get_user_by_id_then_return_user() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USER).concat("/");
        UUID mockUserId = UserMock.userEntity().getId();
        UserDTO mockUserDTO = UserMock.userDTO();

        when(userService.getUserById(mockUserId)).thenReturn(mockUserDTO);

        String response = objectMapper.writeValueAsString(mockUserDTO);

        // ACT
        mockMvc.perform(get(url + mockUserId.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        // ASSERT
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void when_get_user_by_id_then_return_error_404() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USER).concat("/");
        UUID mockNotFoundUserId = UUID.randomUUID();

        when(userService.getUserById(mockNotFoundUserId))
                .thenThrow(NotFoundException.class);

        // ACT
        mockMvc.perform(get(url + mockNotFoundUserId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // ASSERT
        verify(userService, times(1)).getUserById(any());
    }

    @Test
    void given_user_to_create_when_create_user_then_create() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USER);

        CreateUserDTO mockUserToCreate = UserMock.createUserDTO();
        String requestBody = objectMapper.writeValueAsString(mockUserToCreate);


        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
        // ASSERT
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void given_badly_defined_user_to_create_when_create_user_then_return_error_400() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_USER);
        CreateUserDTO userBadlyDefinedDTO = UserMock.userBadlyDefinedDTO();
        String requestBody = objectMapper.writeValueAsString(userBadlyDefinedDTO);

        doThrow(BadRequestException.class).when(userService).createUser(userBadlyDefinedDTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // ASSERT
        verify(userService, times(1)).createUser(any());
    }
}
