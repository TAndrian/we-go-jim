package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.ConflictException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.BookingExceptionEnum;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

    public static final int MAX_PARTICIPANT = 10;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    @Override
    public Set<BookingDTO> getBookings() {
        return bookingMapper.toDTOs(bookingRepository.findAll());
    }

    @Override
    public BookingDTO createOneForUser(UUID userId, CreateBookingDTO bookingToCreate) {

        UserEntity userEntity = retrieveUserById(userId);
        Set<UserEntity> users = new HashSet<>();
        users.add(userEntity);

        // Validate bookingToCreate.
        validateCreateBookingDTO(bookingToCreate);
        BookingEntity createdBooking = createBooking(bookingToCreate);
        createdBooking.setUsers(users);

        return bookingMapper.toDto(createdBooking);
    }

    @Override
    public BookingDTO addBookingToUser(UUID bookingId, UUID userId) {
        return null;
    }

    @Override
    public Set<UserBookingHistoryDTO> getBookingsByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    UserExceptionEnum.USER_NOT_FOUND.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        return bookingMapper.toUserBookingHistoryDTOs(bookingRepository.findByUsers_Id(userId));
    }

    /**
     * Create booking and assign it to the given user.
     *
     * @return new booking with given user assigned on it.
     */
    private BookingEntity createBooking(CreateBookingDTO bookingToCreate) {

        BookingEntity newBooking = new BookingEntity();

        newBooking.setStartTime(bookingToCreate.getStartTime());
        newBooking.setEndTime(bookingToCreate.getEndTime());
        newBooking.setMaxParticipant(1);

        return bookingRepository.saveAndFlush(newBooking);
    }

    /**
     * Check if the current slot is available i.e. there is less than 10 users
     * who booked it.
     *
     * @param maxParticipant max participant.
     */
    private void checkIfSlotIsAvailable(Integer maxParticipant) {
        if (maxParticipant >= MAX_PARTICIPANT) {
            throw new ConflictException(
                    BookingExceptionEnum.BOOKING_MAX_PARTICIPANT_OVER_TEN.value,
                    BookingExceptionEnum.BOOKING_EXCEPTION_CODE.getValue()
            );
        }
    }

    /**
     * Check if the booking to create is badly defined.
     *
     * @param createBookingDTO booking to create.
     */
    private void validateCreateBookingDTO(CreateBookingDTO createBookingDTO) {
        if (createBookingDTO.getEndTime() == null ||
                createBookingDTO.getStartTime() == null) {
            throw new BadRequestException(
                    BookingExceptionEnum.CREATE_BOOKING_BAD_REQUEST.value,
                    BookingExceptionEnum.BOOKING_EXCEPTION_CODE.getValue()
            );
        }
    }

    /**
     * Find user by userId.
     *
     * @param userId given userId.
     * @return user entity if it is present.
     */
    private UserEntity retrieveUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
                    log.info("User not found with id:{}", userId);
                    return new NotFoundException(
                            UserExceptionEnum.USER_NOT_FOUND.getValue(),
                            UserExceptionEnum.USER_EXCEPTION_CODE.value
                    );
                }
        );
    }
}
