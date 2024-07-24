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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
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
    public Set<UserBookingHistoryDTO> getUserBookingHistories(UUID userId) {
        retrieveUserById(userId);
        return bookingMapper.toUserBookingHistoryDTOs(bookingRepository.findByUsers_Id(userId));
    }

    @Override
    public BookingDTO create(UUID userId, CreateBookingDTO createBookingDTO) {

        // Verify booking's validity.
        validateCreateBookingDTO(createBookingDTO);
        checkIfUserHasAlreadyBookedTheSchedule(
                createBookingDTO.getStartTime(),
                createBookingDTO.getEndTime(),
                userId
        );

        UserEntity userEntity = retrieveUserById(userId);

        // Create booking for user.
        BookingEntity createdBooking = createBooking(createBookingDTO, userEntity);

        // Update user's bookings.
        Set<BookingEntity> bookings = userEntity.getBookings();
        bookings.add(createdBooking);
        userEntity.setBookings(bookings);

        return bookingMapper.toDTO(createdBooking);
    }

    @Override
    public BookingDTO addOneToUser(UUID bookingId, UUID userId) {
        BookingEntity bookingEntity = retrieveBookingById(bookingId);

        // Verify booking's validity.
        checkIfUserHasAlreadyBookedTheSchedule(
                bookingEntity.getStartTime(),
                bookingEntity.getEndTime(),
                userId
        );
        checkIfSlotIsAvailable(bookingEntity.getMaxParticipant());

        UserEntity userEntity = retrieveUserById(userId);

        // Update booking for user.
        bookingEntity.setMaxParticipant(bookingEntity.getMaxParticipant() + 1);
        Set<UserEntity> users = bookingEntity.getUsers();
        users.add(userEntity);
        bookingEntity.setUsers(users);
        BookingEntity updatedBookingEntity = bookingRepository.saveAndFlush(bookingEntity);

        // Update user's bookings.
        Set<BookingEntity> bookings = userEntity.getBookings();
        bookings.add(updatedBookingEntity);
        userEntity.setBookings(bookings);
        userRepository.saveAndFlush(userEntity);

        return bookingMapper.toDTO(updatedBookingEntity);
    }

    @Override
    public BookingDTO getById(UUID bookingId) {
        return bookingMapper.toDTO(retrieveBookingById(bookingId));
    }

    /**
     * Create booking.
     *
     * @return new booking.
     */
    private BookingEntity createBooking(CreateBookingDTO bookingToCreate, UserEntity user) {

        BookingEntity newBooking = new BookingEntity();
        Set<UserEntity> users = new HashSet<>();
        users.add(user);

        newBooking.setStartTime(bookingToCreate.getStartTime());
        newBooking.setEndTime(bookingToCreate.getEndTime());
        newBooking.setMaxParticipant(1);


        newBooking.setUsers(users);
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
     * Verify if the user has already booked the same schedule.
     *
     * @param startTime booking start time.
     * @param endTime   booking end time.
     * @param userId    user's id.
     */
    private void checkIfUserHasAlreadyBookedTheSchedule(LocalDateTime startTime,
                                                        LocalDateTime endTime,
                                                        UUID userId) {
        Optional<BookingEntity> existingBooking = bookingRepository.findByStartTimeAndEndTimeAndUsers_Id(
                startTime,
                endTime,
                userId);
        if (existingBooking.isPresent()) {
            throw new ConflictException(
                    BookingExceptionEnum.BOOKING_ALREADY_BOOKED_FOR_CURRENT_USER.getValue(),
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

    private BookingEntity retrieveBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.info("booking not found with id:{}", bookingId);
            return new NotFoundException(
                    BookingExceptionEnum.BOOKING_NOT_FOUND.getValue(),
                    BookingExceptionEnum.BOOKING_EXCEPTION_CODE.getValue()
            );
        });
    }
}
