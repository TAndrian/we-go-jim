package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.service.impl.BookingServiceImpl;
import com.project.we_go_jim.util.BookingMock;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void when_get_all_bookings_then_return_bookings() {
        // ARRANGE
        Set<BookingDTO> mockBookingDTOs = Set.of(BookingMock.bookingDTO());
        List<BookingEntity> mockBookingEntities = List.of(BookingMock.bookingEntity());

        when(bookingRepository.findAll()).thenReturn(mockBookingEntities);
        when(bookingMapper.toDTOs(mockBookingEntities)).thenReturn(mockBookingDTOs);

        // ACT
        Set<BookingDTO> expected = bookingService.getBookings();

        // ASSERT
        assertAll(
                () -> assertEquals(mockBookingDTOs, expected),
                () -> verify(bookingRepository, times(1)).findAll(),
                () -> verify(bookingMapper, times(1)).toDTOs(anyList())
        );
    }

    @Test
    void given_no_booking_when_get_all_bookings_then_return_empty_collection() {
        // ARRANGE
        when(bookingMapper.toDTOs(anyList())).thenReturn(Collections.emptySet());

        // ACT
        Set<BookingDTO> expected = bookingService.getBookings();

        // ASSERT
        assertAll(
                () -> assertEquals(Collections.emptySet(), expected),
                () -> verify(bookingRepository, times(1)).findAll(),
                () -> verify(bookingMapper, times(1)).toDTOs(anyList())
        );
    }

    @Test
    void given_startTime_endTime_user_when_getBookingByStartTimeAndEndTime_then_create_booking() {
        // ARRANGE
        UserEntity mockUserToAddToBooking = UserMock.userEntity();
        BookingEntity mockNewBookingEntity = BookingMock.newBookingEntity();
        LocalDateTime mockStartTime = BookingMock.START_TIME;
        LocalDateTime mockEndTime = BookingMock.END_TIME;

        when(bookingRepository.save(any())).thenReturn(mockNewBookingEntity);

        // ACT
        BookingEntity expected =
                bookingService.getBookingByStartTimeAndEndTime(
                        mockStartTime,
                        mockEndTime,
                        3,
                        mockUserToAddToBooking);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).save(any()),
                () -> verify(bookingRepository, times(1)).findByStartTimeAndEndTime(any(), any()),
                () -> assertEquals(mockNewBookingEntity.getMaxParticipant(), expected.getMaxParticipant()),
                () -> assertThat(mockNewBookingEntity)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(expected)
        );
    }

    @Test
    void given_startTime_endTime_when_getBookingByStartTimeAndEndTime_then_return_booking() {
        // ARRANGE
        UserEntity mockUserToAddToBooking = UserMock.userEntity();
        BookingEntity mockBookingEntity = BookingMock.bookingEntity();
        LocalDateTime mockStartTime = BookingMock.START_TIME;
        LocalDateTime mockEndTime = BookingMock.END_TIME;

        when(bookingRepository.findByStartTimeAndEndTime(mockStartTime, mockEndTime))
                .thenReturn(Optional.ofNullable(mockBookingEntity));

        // ACT
        BookingEntity expected =
                bookingService.getBookingByStartTimeAndEndTime(mockStartTime,
                        mockEndTime,
                        1,
                        mockUserToAddToBooking);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(0)).save(any()),
                () -> verify(bookingRepository, times(1)).findByStartTimeAndEndTime(any(), any()),
                () -> assertThat(mockBookingEntity)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(expected)
        );
    }
}
