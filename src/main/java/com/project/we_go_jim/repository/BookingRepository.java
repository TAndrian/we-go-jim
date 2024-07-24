package com.project.we_go_jim.repository;

import com.project.we_go_jim.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findByUsers_Id(UUID id);

    Optional<BookingEntity> findByStartTimeAndEndTimeAndUsers_Id(LocalDateTime startTime,
                                                                 LocalDateTime endTime,
                                                                 UUID id);
}
