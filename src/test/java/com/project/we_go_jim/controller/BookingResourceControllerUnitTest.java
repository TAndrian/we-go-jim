package com.project.we_go_jim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.controller.resource.BookingResourceController;
import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.exception.BadRequestException;
import com.project.we_go_jim.exception.ConflictException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.service.BookingService;
import com.project.we_go_jim.util.BookingMock;
import com.project.we_go_jim.util.UserMock;
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
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKING;
import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS;
import static com.project.we_go_jim.controller.ResourcesPath.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingResourceController.class)
class BookingResourceControllerUnitTest {

    public static final UUID MOCK_NOT_FOUND_USER_ID = UUID.randomUUID();
    public static final UUID MOCK_NOT_FOUND_BOOKING_ID = UUID.randomUUID();
    public static final BookingDTO MOCK_BOOKING_DTO = BookingMock.bookingDTO();
    public static final UUID MOCK_BOOKING_ID = BookingMock.bookingEntity().getId();
    public static final UUID MOCK_USER_ID = UserMock.USER_ID;
    public static final CreateBookingDTO MOCK_CREATE_BOOKING_DTO = BookingMock.createBookingDTO();
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_getBookings_then_return_bookings() throws Exception {
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
    void given_no_booking_when_getBookings_then_return_empty_collection() throws Exception {
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

    @Test
    void given_userId_when_getUserBookingHistories_then_return_histories() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKINGS + "/")
                .concat(USER + "/")
                .concat(MOCK_USER_ID.toString());
        Set<UserBookingHistoryDTO> mockBookingDTOS = Set.of(BookingMock.userBookingHistoryDTO());

        when(bookingService.getUserBookingHistories(MOCK_USER_ID))
                .thenReturn(mockBookingDTOS);

        String expectedResponseBody = objectMapper.writeValueAsString(mockBookingDTOS);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        // ASSERT
        verify(bookingService, times(1)).getUserBookingHistories(any());
    }

    @Test
    void given_userId_when_getUserBookingHistory_then_return_empty_collections() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKINGS + "/")
                .concat(USER + "/")
                .concat(MOCK_USER_ID.toString());

        String expectedResponseBody = objectMapper.writeValueAsString(Collections.EMPTY_SET);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        // ASSERT
        verify(bookingService, times(1)).getUserBookingHistories(any());
    }

    @Test
    void given_not_found_user_when_getUserBookingHistory_then_return_throw_not_found_exception() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKINGS + "/")
                .concat(USER + "/")
                .concat(MOCK_NOT_FOUND_USER_ID.toString());

        when(bookingService.getUserBookingHistories(MOCK_NOT_FOUND_USER_ID))
                .thenThrow(NotFoundException.class);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // ASSERT
        verify(bookingService, times(1)).getUserBookingHistories(any());
    }

    @Test
    void given_bookingId_when_getById_then_return_booking() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(MOCK_BOOKING_ID.toString());

        when(bookingService.getById(MOCK_BOOKING_ID))
                .thenReturn(MOCK_BOOKING_DTO);

        String expectedResponseBody = objectMapper.writeValueAsString(MOCK_BOOKING_DTO);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        // ASSERT
        verify(bookingService, times(1)).getById(any());
    }

    @Test
    void given_not_found_bookingId_when_getById_then_return_notFound_error() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(MOCK_NOT_FOUND_BOOKING_ID.toString());

        when(bookingService.getById(MOCK_NOT_FOUND_BOOKING_ID))
                .thenThrow(NotFoundException.class);

        // ACT
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // ASSERT
        verify(bookingService, times(1)).getById(any());
    }

    @Test
    void given_userId_and_correct_createBookingDTO_when_create_then_create_booking() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(USER + "/")
                .concat(MOCK_USER_ID.toString());

        BookingDTO mockCreatedBookingDTO = BookingMock.createdBookingDTO();

        when(bookingService.create(MOCK_USER_ID, MOCK_CREATE_BOOKING_DTO))
                .thenReturn(mockCreatedBookingDTO);

        String requestBody = objectMapper.writeValueAsString(MOCK_CREATE_BOOKING_DTO);
        String expectedResponse = objectMapper.writeValueAsString(mockCreatedBookingDTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse));

        // ASSERT
        verify(bookingService, times(1)).create(any(), any());
    }

    @Test
    void given_userId_and_bad_createBookingDTO_when_create_then_return_badRequest_error() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(USER + "/")
                .concat(MOCK_USER_ID.toString());

        CreateBookingDTO createBookingDTO = CreateBookingDTO.builder()
                .startTime(null)
                .endTime(null)
                .build();
        when(bookingService.create(MOCK_USER_ID, createBookingDTO))
                .thenThrow(BadRequestException.class);

        String requestBody = objectMapper.writeValueAsString(createBookingDTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // ASSERT
        verify(bookingService, times(1)).create(any(), any());
    }

    @Test
    void given_userId_and_correct_createBookingDTO_but_slot_already_booked_when_create_return_conflict_error() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(USER + "/")
                .concat(MOCK_USER_ID.toString());

        when(bookingService.create(MOCK_USER_ID, MOCK_CREATE_BOOKING_DTO))
                .thenThrow(ConflictException.class);

        String requestBody = objectMapper.writeValueAsString(MOCK_CREATE_BOOKING_DTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());

        // ASSERT
        verify(bookingService, times(1)).create(any(), any());
    }

    @Test
    void given_not_found_userId_and_correct_createBookingDTO_when_create_then_return_notFound_error() throws Exception {
        // ARRANGE
        final String url = "/".concat(API_BOOKING + "/")
                .concat(USER + "/")
                .concat(MOCK_NOT_FOUND_USER_ID.toString());

        when(bookingService.create(MOCK_NOT_FOUND_USER_ID, MOCK_CREATE_BOOKING_DTO))
                .thenThrow(NotFoundException.class);

        String requestBody = objectMapper.writeValueAsString(MOCK_CREATE_BOOKING_DTO);

        // ACT
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());

        // ASSERT
        verify(bookingService, times(1)).create(any(), any());
    }
}
