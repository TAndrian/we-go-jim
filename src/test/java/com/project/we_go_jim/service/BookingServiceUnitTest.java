package com.project.we_go_jim.service;

import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookingServiceUnitTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void when_get_all_bookings_then_return_bookings() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_no_booking_when_get_all_bookings_then_return_empty_collection() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_booking_when_create_booking_then_create() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_booking_badly_defined_when_create_booking_then_error_400() {
        // ARRANGE

        // ACT

        // ASSERT
    }
}
