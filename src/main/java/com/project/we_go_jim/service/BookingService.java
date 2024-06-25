package com.project.we_go_jim.service;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.model.BookingEntity;
import com.project.we_go_jim.model.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingService {

    Set<BookingDTO> getBookings();

    BookingEntity getBookingByStartTimeAndEndTime(LocalDateTime startTime,
                                                  LocalDateTime endTime,
                                                  Integer maxParticipant,
                                                  UserEntity user);
}
