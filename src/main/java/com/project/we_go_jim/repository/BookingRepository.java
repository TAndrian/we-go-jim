package com.project.we_go_jim.repository;

import com.project.we_go_jim.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    Optional<BookingEntity> findByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);
}
