package com.project.we_go_jim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.controller.resource.BookingResourceController;
import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.service.BookingService;
import com.project.we_go_jim.util.BookingMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingResourceController.class)
class BookingResourceControllerUnitTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_get_all_bookings_then_return_bookings() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKINGS);
        Set<BookingDTO> mockBookings = Set.of(BookingMock.bookingDTO());

        when(bookingService.getBookings()).thenReturn(mockBookings);

        String expectedResponseBody = objectMapper.writeValueAsString(mockBookings);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        // ASSERT
        verify(bookingService, times(1)).getBookings();
    }

    @Test
    void given_no_booking_when_get_all_bookings_then_return_empty_collection() throws Exception {
        final String url = "/".concat(API_BOOKINGS);
        String expectedResponseBody = objectMapper.writeValueAsString(Collections.EMPTY_SET);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        // ASSERT
        verify(bookingService, times(1)).getBookings();
    }
}
