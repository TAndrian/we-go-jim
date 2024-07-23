package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.exception.BadRequestException;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    public static final UUID MOCK_NOT_FOUND_USER_ID = UUID.randomUUID();
    public static final UserEntity MOCK_USER_ENTITY = UserMock.userEntity();
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
    void given_userId_when_getBookingsByUserId_then_return_bookings() {
        // ARRANGE
        List<BookingEntity> mockBookings = new ArrayList<>();
        mockBookings.add(BookingMock.bookingEntity());
        Set<UserBookingHistoryDTO> mockBookingDTOs = new HashSet<>();
        mockBookingDTOs.add(BookingMock.userBookingHistoryDTO());

        when(userRepository.existsById(MOCK_USER_ID)).thenReturn(true);
        when(bookingRepository.findByUsers_Id(MOCK_USER_ID))
                .thenReturn(mockBookings);
        when(bookingMapper.toUserBookingHistoryDTOs(mockBookings))
                .thenReturn(mockBookingDTOs);

        // ACT
        Set<UserBookingHistoryDTO> expected = bookingService.getBookingsByUserId(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toUserBookingHistoryDTOs(anyList()),
                () -> assertEquals(mockBookingDTOs, expected)
        );
    }

    @Test
    void given_userId_when_getBookingsByUserId_then_return_empty_collections() {
        // ARRANGE
        when(userRepository.existsById(MOCK_USER_ID)).thenReturn(true);

        // ACT
        Set<UserBookingHistoryDTO> expected = bookingService.getBookingsByUserId(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toUserBookingHistoryDTOs(anyList()),
                () -> assertEquals(Collections.emptySet(), expected)
        );
    }

    @Test
    void given_not_found_user_when_getBookingsByUserId_then_return_throw_not_found_exception() {
        // ARRANGE
        when(userRepository.existsById(MOCK_NOT_FOUND_USER_ID)).thenReturn(false);

        // ACT
        NotFoundException exception =
                assertThrows(
                        NotFoundException.class,
                        () -> bookingService.getBookingsByUserId(MOCK_NOT_FOUND_USER_ID)
                );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).existsById(any()),
                () -> verify(bookingRepository, times(0)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(0)).toDTOs(anyList()),
                () -> assertEquals(UserExceptionEnum.USER_NOT_FOUND.getValue(), exception.getMessage())
        );
    }

    @Test
    void given_userId_and_correct_createBookingDTO_when_createOneForUser_then_create_booking() {
        // ARRANGE
        CreateBookingDTO mockCreateBookingDTO = BookingMock.createBookingDTO();
        BookingEntity mockCreatedBookingEntity = BookingMock.createdBookingEntity();
        BookingDTO mockCreatedBookingDTO = BookingMock.createdBookingDTO();

        when(userRepository.findById(MOCK_USER_ID)).thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));
        when(bookingRepository.save(any())).thenReturn(mockCreatedBookingEntity);
        when(bookingMapper.toDto(mockCreatedBookingEntity)).thenReturn(mockCreatedBookingDTO);

        // ACT
        BookingDTO expected = bookingService.createOneForUser(MOCK_USER_ID, mockCreateBookingDTO);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(1)).save(any()),
                () -> verify(bookingMapper, times(1)).toDto(any()),
                () -> assertEquals(mockCreatedBookingDTO, expected)
        );
    }

    @Test
    void given_not_found_userId_and_correct_createBookingDTO_when_createOneForUser_then_throw_notFound_error() {
        // ARRANGE
        CreateBookingDTO mockCreateBookingDTO = BookingMock.createBookingDTO();

        // ACT
        NotFoundException exception =
                assertThrows(
                        NotFoundException.class,
                        () -> bookingService.createOneForUser(MOCK_NOT_FOUND_USER_ID, mockCreateBookingDTO)
                );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(0)).save(any()),
                () -> verify(bookingMapper, times(0)).toDto(any()),
                () -> assertEquals(exception.getMessage(), UserExceptionEnum.USER_NOT_FOUND.getValue())
        );
    }

    @Test
    void given_userId_and_bad_createBookingDTO_when_createOneForUser_then_throw_badRequest_error() {
        // ARRANGE
        CreateBookingDTO mockBadCreateBookingDTO = BookingMock.badCreateBookingDTO();
        when(userRepository.findById(MOCK_USER_ID)).thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));

        // ACT
        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> bookingService.createOneForUser(MOCK_USER_ID, mockBadCreateBookingDTO)
                );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(0)).save(any()),
                () -> verify(bookingMapper, times(0)).toDto(any()),
                () -> assertEquals(BookingExceptionEnum.CREATE_BOOKING_BAD_REQUEST.getValue(), exception.getMessage())
        );
    }
}
