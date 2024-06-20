package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.impl.UserServiceImpl;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void when_get_all_users_then_return_users() {
        // ARRANGE
        List<UserDTO> mockUserDTOs = Collections.singletonList(UserMock.userDTO());
        List<UserEntity> mockUserEntities = Collections.singletonList(UserMock.userEntity());

        when(userRepository.findAll()).thenReturn(mockUserEntities);
        when(userMapper.toDTOs(mockUserEntities)).thenReturn(mockUserDTOs);

        // ACT
        List<UserDTO> expected = userService.getUsers();

        // ASSERT
        assertAll(
                () -> assertEquals(expected, mockUserDTOs),
                () -> verify(userMapper, times(1)).toDTOs(anyList()),
                () -> verify(userRepository, times(1)).findAll()
        );

    }

    @Test
    void given_no_users_when_get_all_users_then_return_empty_collection() {
        // ARRANGE

        // ACT
        List<UserDTO> expected = userService.getUsers();

        // ASSERT
        assertAll(
                () -> assertEquals(expected, Collections.EMPTY_LIST),
                () -> verify(userMapper, times(1)).toDTOs(anyList()),
                () -> verify(userRepository, times(1)).findAll()
        );
    }

    @Test
    void given_userId_when_get_user_by_id_then_return_user() {
        // ARRANGE
        UUID mockUserId = UserMock.USER_ID;
        UserEntity mockUserEntity = UserMock.userEntity();
        UserDTO mockUserDTO = UserMock.userDTO();

        when(userRepository.findById(mockUserId)).thenReturn(Optional.ofNullable(mockUserEntity));
        when(userMapper.toDTO(mockUserEntity)).thenReturn(mockUserDTO);

        // ACT
        UserDTO expected = userService.getUserById(mockUserId);

        // ASSERT
        assertAll(
                () -> assertEquals(expected, mockUserDTO),
                () -> verify(userMapper, times(1)).toDTO(any()),
                () -> verify(userRepository, times(1)).findById(any())
        );
    }

    @Test
    void when_get_user_by_id_then_return_error_404() {
        // ARRANGE

        // ACT
        assertThrows(NotFoundException.class,
                () -> userService.getUserById(any()));

        // ASSERT
        assertAll(
                () -> verify(userMapper, times(0)).toDTO(any()),
                () -> verify(userRepository, times(1)).findById(any())
        );
    }

    @Test
    void given_user_to_create_when_create_user_then_create() {
        // ARRANGE
        CreateUserDTO mockUserDTOToCreate = UserMock.createUserDTO();
        UserDTO mockSavedUserDto = UserMock.createdUserDTO();
        UserEntity mockUserEntityToCreate = UserMock.createUserEntity();

        when(userMapper.toEntity(mockUserDTOToCreate)).thenReturn(mockUserEntityToCreate);
        when(userRepository.save(mockUserEntityToCreate)).thenReturn(mockUserEntityToCreate);
        when(userMapper.toDTO(any())).thenReturn(mockSavedUserDto);

        // ACT
        UserDTO expected = userService.createUser(mockUserDTOToCreate);

        // ASSERT
        assertAll(
                () -> assertThat(expected)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(mockUserDTOToCreate),
                () -> verify(userMapper, times(1)).toEntity(any()),
                () -> verify(userRepository, times(1)).save(any()),
                () -> verify(userMapper, times(1)).toDTO(any())
        );
    }

    @Test
    void given_badly_defined_user_to_create_when_create_user_then_return_error_400() {
        // ARRANGE
        CreateUserDTO mockUserBadlyDefinedDTO = UserMock.userBadlyDefinedDTO();

        // ACT
        assertThrows(BadRequestException.class,
                () -> userService.createUser(mockUserBadlyDefinedDTO));

        // ASSERT
        assertAll(
                () -> verify(userMapper, times(0)).toEntity(any()),
                () -> verify(userRepository, times(0)).save(any()),
                () -> verify(userMapper, times(0)).toDTO(any())
        );
    }
}
