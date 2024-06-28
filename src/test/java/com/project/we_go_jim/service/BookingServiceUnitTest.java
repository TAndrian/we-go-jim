package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.exception.ConflictException;
import com.project.we_go_jim.exception.NotFoundException;
import com.project.we_go_jim.exception.enums.BookingExceptionEnum;
import com.project.we_go_jim.exception.enums.UserExceptionEnum;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.impl.BookingServiceImpl;
import com.project.we_go_jim.util.BookingMock;
import com.project.we_go_jim.util.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {
    public static final UUID MOCK_USER_ID = UserMock.USER_ID;
    private static final LocalDateTime mockStartTime = BookingMock.START_TIME;
    private static final LocalDateTime mockEndTime = BookingMock.END_TIME;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private UserRepository userRepository;

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
    void given_user_and_available_schedule_when_getBookingByStartTimeAndEndTime_then_create_booking() {
        // ARRANGE
        Integer mockMaxParticipant = 0;

        BookingEntity mockNewBookingEntity = BookingMock.newBookingEntity();
        UserEntity mockUserToAssignToBookingEntity = UserMock.userWithoutBookingEntity();

        when(bookingRepository.save(any()))
                .thenReturn(mockNewBookingEntity);

        // ACT
        BookingEntity expected = bookingService.getBookingByStartTimeAndEndTime(
                mockStartTime,
                mockEndTime,
                mockMaxParticipant,
                mockUserToAssignToBookingEntity);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).save(any()),
                () -> verify(bookingRepository, times(1))
                        .findByStartTimeAndEndTime(any(), any()),
                () -> assertThat(mockNewBookingEntity)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(expected)
        );
    }

    @Test
    void given_user_and_available_schedule_when_getBookingByStartTimeAndEndTime_then_return_booking() {
        // ARRANGE
        Integer mockMaxParticipant = BookingMock.bookingEntity().getMaxParticipant();

        BookingEntity mockBookingFoundByStartTimeAndEndTime = BookingMock.bookingEntity();
        UserEntity mockUserToAssignToBookingEntity = UserMock.userEntity();

        when(bookingRepository.findByStartTimeAndEndTime(mockStartTime, mockEndTime))
                .thenReturn(Optional.ofNullable(mockBookingFoundByStartTimeAndEndTime));

        // ACT
        BookingEntity expected = bookingService.getBookingByStartTimeAndEndTime(
                mockStartTime,
                mockEndTime,
                mockMaxParticipant,
                mockUserToAssignToBookingEntity);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(0)).save(any()),
                () -> verify(bookingRepository, times(1))
                        .findByStartTimeAndEndTime(any(), any()),
                () -> assertThat(mockBookingFoundByStartTimeAndEndTime)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt", "updatedAt")
                        .isEqualTo(expected)
        );
    }

    @Test
    void given_user_and_unavailable_schedule_when_getBookingByStartTimeAndEndTime_then_throw_conflict_error() {
        // ARRANGE
        Integer mockMaxParticipant = BookingMock.MAX_PARTICIPANT;
        UserEntity mockUserToAssignToBookingEntity = UserMock.userEntity();

        // ACT
        ConflictException exception = assertThrows(ConflictException.class, () ->
                bookingService.getBookingByStartTimeAndEndTime(
                        mockStartTime,
                        mockEndTime,
                        mockMaxParticipant,
                        mockUserToAssignToBookingEntity)
        );

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(0)).save(any()),
                () -> verify(bookingRepository, times(0))
                        .findByStartTimeAndEndTime(any(), any()),
                () -> assertEquals(exception.getMessage(),
                        BookingExceptionEnum.BOOKING_MAX_PARTICIPANT_OVER_TEN.getValue())
        );
    }

    @Test
    void given_userId_when_getBookingsByUserId_then_return_bookings() {
        // ARRANGE
        List<BookingEntity> mockBookings = new ArrayList<>();
        mockBookings.add(BookingMock.bookingEntity());
        Set<BookingDTO> mockBookingDTOs = new HashSet<>();
        mockBookingDTOs.add(BookingMock.bookingDTO());

        when(userRepository.existsById(MOCK_USER_ID)).thenReturn(true);
        when(bookingRepository.findByUsers_Id(MOCK_USER_ID))
                .thenReturn(mockBookings);
        when(bookingMapper.toDTOs(mockBookings))
                .thenReturn(mockBookingDTOs);

        // ACT
        Set<BookingDTO> expected = bookingService.getBookingsByUserId(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toDTOs(anyList()),
                () -> assertEquals(mockBookingDTOs, expected)
        );
    }

    @Test
    void given_userId_when_getBookingsByUserId_then_return_empty_collections() {
        // ARRANGE
        when(userRepository.existsById(MOCK_USER_ID)).thenReturn(true);

        // ACT
        Set<BookingDTO> expected = bookingService.getBookingsByUserId(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toDTOs(anyList()),
                () -> assertEquals(Collections.emptySet(), expected)
        );
    }

    @Test
    void given_not_found_user_when_getBookingsByUserId_then_return_throw_not_found_exception() {
        // ARRANGE
        UUID mockNotFoundUserId = UUID.randomUUID();
        when(userRepository.existsById(mockNotFoundUserId)).thenReturn(false);

        // ACT
        NotFoundException exception =
                assertThrows(
                        NotFoundException.class,
                        () -> bookingService.getBookingsByUserId(mockNotFoundUserId)
                );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(0)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(0)).toDTOs(anyList()),
                () -> assertEquals(UserExceptionEnum.USER_NOT_FOUND.getValue(), exception.getMessage())
        );
    }
}
