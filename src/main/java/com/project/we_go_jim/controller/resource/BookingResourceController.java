package com.project.we_go_jim.controller.resource;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS;
import static com.project.we_go_jim.controller.ResourcesPath.USER;

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

    @GetMapping(API_BOOKINGS + "/" + USER + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Set<BookingDTO> getBookingsByUserId(@PathVariable UUID id) {
        return bookingService.getBookingsByUserId(id);
    }
}
