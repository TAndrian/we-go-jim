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
     * Create booking for user.
     *
     * @param userId          target user's id.
     * @param bookingToCreate booking event to create.
     * @return Created booking which is assigned to the target user.
     */
    BookingDTO createOneForUser(UUID userId, CreateBookingDTO bookingToCreate);

    /**
     * Add user to an existing booking.
     *
     * @param bookingId target booking id.
     * @param userId    target user to add to the booking.
     * @return Booking where the user is added.
     */
    BookingDTO addBookingToUser(UUID bookingId, UUID userId);

    /**
     * Retrieve all user's bookings.
     *
     * @param userId given userId.
     * @return all user's booking.
     */
    Set<UserBookingHistoryDTO> getBookingsByUserId(UUID userId);
}
