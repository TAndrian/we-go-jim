package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.exception.ConflictException;
import com.project.we_go_jim.exception.enums.BookingExceptionEnum;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

    public static final int MAX_PARTICIPANT = 10;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

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
        Optional<BookingEntity> booking = bookingRepository.findByStartTimeAndEndTime(startTime, endTime);
        checkIfSlotIsAvailable(maxParticipant);
        return booking.orElseGet(() -> createBooking(startTime, endTime, user));
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
