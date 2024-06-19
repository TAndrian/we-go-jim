package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;

import java.util.Set;

public interface BookingService {
    Set<BookingDTO> createBookings(Set<BookingDTO> bookingDTOS);

    Set<BookingDTO> getBookings();
}
