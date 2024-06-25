package com.project.we_go_jim.mapper;

import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.model.BookingEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Set<BookingDTO> toDTOs(List<BookingEntity> bookingEntities);
}
