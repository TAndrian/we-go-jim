package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.impl.UserServiceImpl;
import com.project.we_go_jim.util.BookingMock;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookingService bookingService;

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
                () -> assertEquals(Collections.EMPTY_LIST, expected),
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
    void when_get_user_by_id_then_return_error_404() throws NotFoundException {
        // ARRANGE
        UUID notFoundUserId = UUID.randomUUID();

        // ACT
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(notFoundUserId));

        // ASSERT
        assertAll(
                () -> assertEquals(exception.getMessage(), UserExceptionEnum.USER_NOT_FOUND.value),
                () -> verify(userMapper, times(0)).toDTO(any()),
                () -> verify(userRepository, times(1)).findById(any())
        );
    }

    @Test
    void given_user_to_create_when_create_user_then_create() {
        // ARRANGE
        CreateUserDTO mockUserDTOToCreate = UserMock.createUserDTO();
        UserEntity mockUserEntityToCreate = UserMock.createUserEntity();

        when(userMapper.toEntity(mockUserDTOToCreate)).thenReturn(mockUserEntityToCreate);
        when(userRepository.save(mockUserEntityToCreate)).thenReturn(mockUserEntityToCreate);

        // ACT
        userService.createUser(mockUserDTOToCreate);

        // ASSERT
        assertAll(
                () -> verify(userMapper, times(1)).toEntity(any()),
                () -> verify(userRepository, times(1)).save(any())
        );
    }

    @Test
    void given_badly_defined_user_to_create_when_create_user_then_return_error_400() {
        // ARRANGE
        CreateUserDTO mockUserBadlyDefinedDTO = UserMock.userBadlyDefinedDTO();

        // ACT
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> userService.createUser(mockUserBadlyDefinedDTO));

        // ASSERT
        assertAll(
                () -> assertEquals(badRequestException.getMessage(),
                        UserExceptionEnum.USER_BAD_REQUEST_FIRSTNAME.value),

                () -> verify(userMapper, times(0)).toEntity(any()),
                () -> verify(userRepository, times(0)).save(any()),
                () -> verify(userMapper, times(0)).toDTO(any())
        );
    }

    @Test
    void given_email_already_taken_when_create_then_return_error_400() {
        // ARRANGE
        CreateUserDTO mockUserBadlyDefinedDTO = UserMock.userBadlyDefinedDTO();
        mockUserBadlyDefinedDTO.setFirstName("first");
        mockUserBadlyDefinedDTO.setLastName("last");
        mockUserBadlyDefinedDTO.setEmail("t@t.com");

        when(userRepository.existsByEmailIgnoreCase(mockUserBadlyDefinedDTO.getEmail())).thenReturn(true);

        // ACT
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> userService.createUser(mockUserBadlyDefinedDTO));

        // ASSERT
        assertAll(
                () -> assertEquals(badRequestException.getMessage(),
                        UserExceptionEnum.USER_BAD_REQUEST_EMAIL_ALREADY_TAKEN.value),

                () -> verify(userMapper, times(0)).toEntity(any()),
                () -> verify(userRepository, times(0)).save(any()),
                () -> verify(userMapper, times(0)).toDTO(any())
        );
    }

    @Test
    void when_addBookingToUser_then_add_booking() {
        // ARRANGE
        UUID mockUserId = UserMock.USER_ID;
        LocalDateTime mockStartTime = BookingMock.START_TIME;
        LocalDateTime mockEndTime = BookingMock.END_TIME;
        Integer mockMaxParticipant = BookingMock.bookingDTO().getMaxParticipant();

        UserEntity mockUserWithoutBookingEntity = UserMock.userWithoutBookingEntity();
        BookingEntity mockBookingAssignedToUserEntity = BookingMock.bookingAssignedToUser();
        UserEntity mockUserAssignedToBookingEntity = UserMock.userAssignedToBookingEntity();
        UserDTO mockUserAssignedToBookingDTO = UserMock.userAssignedToBookingDTO();

        when(userRepository.findById(mockUserId))
                .thenReturn(Optional.ofNullable(mockUserWithoutBookingEntity));

        when(bookingService.getBookingByStartTimeAndEndTime(
                mockStartTime,
                mockEndTime,
                mockMaxParticipant,
                mockUserWithoutBookingEntity
        )).thenReturn(mockBookingAssignedToUserEntity);

        when(userRepository.save(any()))
                .thenReturn(mockUserAssignedToBookingEntity);

        when(userMapper.toDTO(any()))
                .thenReturn(mockUserAssignedToBookingDTO);

        // ACT
        UserDTO expected = userService.addBookingToUser(mockUserId, mockStartTime, mockEndTime, mockMaxParticipant);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingService, times(1))
                        .getBookingByStartTimeAndEndTime(any(), any(), any(), any()),
                () -> verify(userMapper, times(1)).toDTO(any()),
                () -> assertThat(mockUserAssignedToBookingDTO)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(expected)
        );
    }
}
