package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Set<BookingDTO> getBookings() {
        return bookingMapper.toDTOs(bookingRepository.findAll());
    }
}
