package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;

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
     * Retrieve all user's bookings.
     *
     * @param userId given userId.
     * @return all user's booking.
     */
    Set<UserBookingHistoryDTO> getUserBookingHistories(UUID userId);

    /**
     * Create booking for user.
     *
     * @param userId          user's id.
     * @param bookingToCreate booking to create.
     * @return created booking and the user assigned on it.
     */
    BookingDTO create(UUID userId, CreateBookingDTO bookingToCreate);

    BookingDTO addOneToUser(UUID bookingId, UUID userId);

    BookingDTO getById(UUID bookingId);
}
