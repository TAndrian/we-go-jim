package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface BookingService {

    /**
     * Retrieves all bookings.
     *
     * @return bookings.
     */
    Set<BookingDTO> getBookings();

    /**
     * Retrieve booking based on a given startTime, endTime and maxParticipant.
     * If the booking does not exist, it will be created and assign it to the given user.
     *
     * @param startTime      startTIme.
     * @param endTime        endTime.
     * @param maxParticipant maxParticipant.
     * @param user           existing user.
     * @return booking to assign or which has been assigned to the given user.
     */
    BookingEntity getBookingByStartTimeAndEndTime(LocalDateTime startTime,
                                                  LocalDateTime endTime,
                                                  Integer maxParticipant,
                                                  UserEntity user);

    /**
     * Retrieve all user's bookings.
     *
     * @param userId given userId.
     * @return all user's booking.
     */
    Set<UserBookingHistoryDTO> getBookingsByUserId(UUID userId);
}
