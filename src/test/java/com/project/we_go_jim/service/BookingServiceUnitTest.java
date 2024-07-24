package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.exception.BadRequestException;
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
import org.junit.jupiter.api.Order;
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
    private static final UUID MOCK_USER_ID = UserMock.USER_ID;
    private static final UserEntity MOCKER_USER_ENTITY = UserMock.userEntity();
    private static final UUID MOCK_NOT_FOUND_BOOKING_ID = UUID.randomUUID();
    private static final BookingEntity MOCK_BOOKING_ENTITY = BookingMock.bookingEntity();
    private static final UUID MOCK_BOOKING_ID = BookingMock.bookingEntity().getId();
    private static final BookingDTO MOCK_BOOKING_DTO = BookingMock.bookingDTO();
    private static final CreateBookingDTO MOCK_CREATE_BOOKING_DTO = BookingMock.createBookingDTO();
    private static final UUID MOCK_NOT_FOUND_USER_ID = UUID.randomUUID();

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void when_getBookings_then_return_bookings() {
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
    void given_no_booking_when_getBookings_then_return_empty_collection() {
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
    void given_userId_when_getUserBookingHistories_then_return_bookings() {
        // ARRANGE
        List<BookingEntity> mockBookings = new ArrayList<>();
        mockBookings.add(BookingMock.bookingEntity());
        Set<UserBookingHistoryDTO> mockBookingDTOs = new HashSet<>();
        mockBookingDTOs.add(BookingMock.userBookingHistoryDTO());

        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.ofNullable(MOCKER_USER_ENTITY));
        when(bookingRepository.findByUsers_Id(MOCK_USER_ID))
                .thenReturn(mockBookings);
        when(bookingMapper.toUserBookingHistoryDTOs(mockBookings))
                .thenReturn(mockBookingDTOs);

        // ACT
        Set<UserBookingHistoryDTO> expected = bookingService.getUserBookingHistories(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toUserBookingHistoryDTOs(anyList()),
                () -> assertEquals(mockBookingDTOs, expected)
        );
    }

    @Test
    void given_userId_when_getUserBookingHistories_then_return_empty_collections() {
        // ARRANGE
        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.ofNullable(MOCKER_USER_ENTITY));
        // ACT
        Set<UserBookingHistoryDTO> expected = bookingService.getUserBookingHistories(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(1)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(1)).toUserBookingHistoryDTOs(anyList()),
                () -> assertEquals(Collections.emptySet(), expected)
        );
    }

    @Test
    void given_not_found_user_when_getUserBookingHistories_then_return_throw_not_found_exception() {
        // ARRANGE

        // ACT
        NotFoundException exception =
                assertThrows(
                        NotFoundException.class,
                        () -> bookingService.getUserBookingHistories(MOCK_NOT_FOUND_USER_ID)
                );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(0)).findByUsers_Id(any()),
                () -> verify(bookingMapper, times(0)).toDTOs(anyList()),
                () -> assertEquals(UserExceptionEnum.USER_NOT_FOUND.getValue(), exception.getMessage())
        );
    }

    @Test
    void given_bookingId_when_getById_then_return_booking() {
        // ARRANGE
        when(bookingRepository.findById(MOCK_BOOKING_ID))
                .thenReturn(Optional.ofNullable(MOCK_BOOKING_ENTITY));
        when(bookingMapper.toDTO(MOCK_BOOKING_ENTITY))
                .thenReturn(MOCK_BOOKING_DTO);

        // ACT
        BookingDTO expected = bookingService.getById(MOCK_BOOKING_ID);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).findById(any()),
                () -> verify(bookingMapper, times(1)).toDTO(any()),
                () -> assertEquals(MOCK_BOOKING_DTO, expected)
        );
    }

    @Test
    void given_not_found_bookingId_when_getById_then_return_notFound_error() {
        // ARRANGE

        // ACT
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getById(MOCK_NOT_FOUND_BOOKING_ID)
        );

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).findById(any()),
                () -> verify(bookingMapper, times(0)).toDTO(any()),
                () -> assertEquals(BookingExceptionEnum.BOOKING_NOT_FOUND.getValue(), exception.getMessage())
        );
    }

    @Test
    void given_userId_and_correct_createBookingDTO_when_create_then_create_booking() {
        // ARRANGE
        CreateBookingDTO mockCreateBookingDTO = BookingMock.createBookingDTO();
        BookingEntity mockCreatedBookingEntity = BookingMock.createdBookingEntity();
        BookingDTO mockCreatedBookingDTO = BookingMock.createdBookingDTO();

        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.of(MOCKER_USER_ENTITY));
        when(bookingRepository.saveAndFlush(any()))
                .thenReturn(mockCreatedBookingEntity);
        when(bookingMapper.toDTO(mockCreatedBookingEntity))
                .thenReturn(mockCreatedBookingDTO);

        // ACT
        BookingDTO expected = bookingService.create(MOCK_USER_ID, mockCreateBookingDTO);

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).findByStartTimeAndEndTimeAndUsers_Id(
                        any(),
                        any(),
                        any()
                ),
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(1)).saveAndFlush(any()),
                () -> verify(bookingMapper, times(1)).toDTO(any()),
                () -> assertEquals(mockCreatedBookingDTO, expected)
        );
    }

    @Test
    @Order(1)
    void given_userId_and_bad_createBookingDTO_when_create_then_return_badRequest_error() {
        // ARRANGE
        final CreateBookingDTO mockCreateBookingDTO = MOCK_CREATE_BOOKING_DTO;
        mockCreateBookingDTO.setStartTime(null);

        // ACT
        BadRequestException exceptionOnStartTime = assertThrows(
                BadRequestException.class,
                () -> bookingService.create(MOCK_USER_ID, mockCreateBookingDTO)
        );

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(0)).findByStartTimeAndEndTimeAndUsers_Id(
                        any(),
                        any(),
                        any()
                ),
                () -> verify(userRepository, times(0)).findById(any()),
                () -> verify(bookingRepository, times(0)).saveAndFlush(any()),
                () -> verify(bookingMapper, times(0)).toDTO(any()),
                () -> assertEquals(BookingExceptionEnum.CREATE_BOOKING_BAD_REQUEST.getValue(),
                        exceptionOnStartTime.getMessage())
        );

        // ARRANGE
        mockCreateBookingDTO.setEndTime(null);

        // ACT
        BadRequestException exceptionOnEndTime = assertThrows(
                BadRequestException.class,
                () -> bookingService.create(MOCK_USER_ID, mockCreateBookingDTO)
        );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(0)).findById(any()),
                () -> verify(bookingRepository, times(0)).findByStartTimeAndEndTimeAndUsers_Id(
                        any(),
                        any(),
                        any()
                ),
                () -> verify(bookingRepository, times(0)).saveAndFlush(any()),
                () -> verify(bookingMapper, times(0)).toDTO(any()),
                () -> assertEquals(BookingExceptionEnum.CREATE_BOOKING_BAD_REQUEST.getValue(),
                        exceptionOnEndTime.getMessage())
        );
    }

    @Test
    @Order(2)
    void given_userId_and_correct_createBookingDTO_but_slot_already_booked_when_create_return_conflict_error() {
        // ARRANGE

        when(bookingRepository.findByStartTimeAndEndTimeAndUsers_Id(
                MOCK_CREATE_BOOKING_DTO.getStartTime(),
                MOCK_CREATE_BOOKING_DTO.getEndTime(),
                MOCK_USER_ID
        )).thenReturn(Optional.ofNullable(MOCK_BOOKING_ENTITY));

        // ACT
        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> bookingService.create(MOCK_USER_ID, MOCK_CREATE_BOOKING_DTO)
        );

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).findByStartTimeAndEndTimeAndUsers_Id(
                        any(),
                        any(),
                        any()
                ),
                () -> verify(userRepository, times(0)).findById(any()),
                () -> verify(bookingRepository, times(0)).saveAndFlush(any()),
                () -> verify(bookingMapper, times(0)).toDTO(any()),
                () -> assertEquals(BookingExceptionEnum.BOOKING_ALREADY_BOOKED_FOR_CURRENT_USER.getValue(),
                        exception.getMessage())
        );
    }

    @Test
    @Order(3)
    void given_not_found_userId_and_correct_createBookingDTO_when_create_then_return_notFound_error() {
        // ARRANGE
        MOCK_CREATE_BOOKING_DTO.setStartTime(LocalDateTime.now());
        MOCK_CREATE_BOOKING_DTO.setEndTime(LocalDateTime.now());

        // ACT
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.create(MOCK_NOT_FOUND_USER_ID, MOCK_CREATE_BOOKING_DTO)
        );

        // ASSERT
        assertAll(
                () -> verify(bookingRepository, times(1)).findByStartTimeAndEndTimeAndUsers_Id(
                        any(),
                        any(),
                        any()
                ),
                () -> verify(userRepository, times(1)).findById(any()),
                () -> verify(bookingRepository, times(0)).saveAndFlush(any()),
                () -> verify(bookingMapper, times(0)).toDTO(any()),
                () -> assertEquals(UserExceptionEnum.USER_NOT_FOUND.getValue(),
                        exception.getMessage())
        );
    }

    @Test
    void given_bookingId_and_userId_when_addOneToUser_then_add_user_to_booking() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_not_found_ids_when_addOneToUser_then_return_notFound_error() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_bookingId_and_userId_and_unavailable_slot_when_addOneToUser_then_add_user_to_booking() {
        // ARRANGE

        // ACT

        // ASSERT
    }
}
