package com.project.we_go_jim.controller.resource;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_ADD_BOOKING_TO_USER;
import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS;
import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS_USER_BY_ID;
import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKING_BY_ID;
import static com.project.we_go_jim.controller.ResourcesPath.API_CREATE_BOOKING_FOR_USER;

@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class BookingResourceController {

    private BookingService bookingService;

    @GetMapping(API_BOOKINGS)
    @ResponseStatus(HttpStatus.OK)
    public Set<BookingDTO> getBookings() {
        return bookingService.getBookings();
    }

    @GetMapping(API_BOOKINGS_USER_BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public Set<UserBookingHistoryDTO> getUserBookingHistory(@PathVariable UUID id) {
        return bookingService.getUserBookingHistories(id);
    }

    @GetMapping(API_BOOKING_BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO getById(@PathVariable UUID bookingId) {
        return bookingService.getById(bookingId);
    }

    @PostMapping(API_ADD_BOOKING_TO_USER)
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO addUserToBooking(@PathVariable UUID bookingId, @PathVariable UUID userId) {
        return bookingService.addOneToUser(bookingId, userId);
    }

    @PostMapping(API_CREATE_BOOKING_FOR_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO createBookingForUser(@PathVariable UUID userId, @RequestBody CreateBookingDTO createBookingDTO) {
        return bookingService.create(userId, createBookingDTO);
    }
}
