package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.dto.BookingDTO;
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

    /**
     * Get booking by start time and end time and assign it to the given user.
     *
     * @param startTime start time.
     * @param endTime   end time.
     * @param user      user to assign to the current booking slot.
     * @return Booking with user assigned to the slot if it exists, create a new one and assign it to the
     * given user otherwise.
     */
    public BookingEntity getBookingByStartTimeAndEndTime(LocalDateTime startTime,
                                                         LocalDateTime endTime,
                                                         Integer maxParticipant,
                                                         UserEntity user) {
        checkIfSlotIsAvailable(maxParticipant);
        Optional<BookingEntity> booking = bookingRepository.findByStartTimeAndEndTime(startTime, endTime);
        return booking.orElseGet(() -> createBooking(startTime, endTime, user));
    }

    @Override
    public Set<BookingDTO> getBookingsByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    UserExceptionEnum.USER_NOT_FOUND.value,
                    UserExceptionEnum.USER_EXCEPTION_CODE.value
            );
        }
        return bookingMapper.toDTOs(bookingRepository.findByUsers_Id(userId));
    }

    /**
     * Create booking and assign it to the given user.
     *
     * @param startTime start time.
     * @param endTime   end time.
     * @param user      given user.
     * @return new booking with given user assigned on it.
     */
    private BookingEntity createBooking(LocalDateTime startTime, LocalDateTime endTime, UserEntity user) {
        BookingEntity newBooking = new BookingEntity();
        Set<UserEntity> users = newBooking.getUsers();

        newBooking.setStartTime(startTime);
        newBooking.setEndTime(endTime);

        // Update users on the current booking.
        users.add(user);
        newBooking.setUsers(users);

        return bookingRepository.save(newBooking);
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
}
