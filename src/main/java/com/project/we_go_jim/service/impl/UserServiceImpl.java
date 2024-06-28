package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.BookingService;
import com.project.we_go_jim.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final BookingService bookingService;

    @Override
    public List<UserDTO> getUsers() {
        return userMapper.toDTOs(userRepository.findAll());
    }

    @Override
    public void createUser(CreateUserDTO userToCreate) {
        checkUserToCreate(userToCreate);
        UserEntity userEntity = userMapper.toEntity(userToCreate);
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        UserEntity user = findUserById(userId);
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO addBookingToUser(UUID userId,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime,
                                    Integer maxParticipant) {
        UserEntity user = findUserById(userId);
        BookingEntity availableBooking =
                bookingService.getBookingByStartTimeAndEndTime(
                        startTime,
                        endTime,
                        maxParticipant,
                        user
                );
        availableBooking.setMaxParticipant(availableBooking.getMaxParticipant() + 1);
        Set<BookingEntity> userBookings = user.getBookings();
        userBookings.add(availableBooking);
        user.setBookings(userBookings);
        UserEntity userAssignedToBooking = userRepository.save(user);
        return userMapper.toDTO(userAssignedToBooking);
    }

    /**
     * Retrieve user by id.
     *
     * @param userId given userId.
     * @return Corresponding user of the given userId.
     */
    private UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
                    log.info("User not found with id:{}", userId);
                    return new NotFoundException(
                            UserExceptionEnum.USER_NOT_FOUND.value,
                            UserExceptionEnum.USER_EXCEPTION_CODE.value
                    );
                }
        );
    }

    /**
     * Check if the user to create is badly defined.
     *
     * @param userToCreate given user to create.
     */
    private void checkUserToCreate(CreateUserDTO userToCreate) {
        if (userToCreate.getFirstName() == null || userToCreate.getFirstName().isEmpty()) {
            throw new BadRequestException(
                    UserExceptionEnum.USER_BAD_REQUEST_FIRSTNAME.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        if (userToCreate.getLastName() == null || userToCreate.getLastName().isEmpty()) {
            throw new BadRequestException(
                    UserExceptionEnum.USER_BAD_REQUEST_LASTNAME.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        if (userToCreate.getEmail() == null || userToCreate.getEmail().isEmpty()) {
            throw new BadRequestException(
                    UserExceptionEnum.USER_BAD_REQUEST_EMAIL.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        boolean isEmailAlreadyTaken = userRepository.existsByEmailIgnoreCase(userToCreate.getEmail());
        if (isEmailAlreadyTaken) {
            throw new BadRequestException(
                    UserExceptionEnum.USER_BAD_REQUEST_EMAIL_ALREADY_TAKEN.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        if (userToCreate.getPassword() == null || userToCreate.getPassword().isEmpty()) {
            throw new BadRequestException(
                    UserExceptionEnum.USER_BAD_REQUEST_PASSWORD.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
    }
}
