package com.project.we_go_jim.controller;

import com.project.we_go_jim.controller.resource.BookingResourceController;
import com.project.we_go_jim.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingResourceController.class)
public class BookingResourceControllerUnitTest {

    @Mock
    private BookingService bookingService;


    @InjectMocks
    private BookingResourceController controller;

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
