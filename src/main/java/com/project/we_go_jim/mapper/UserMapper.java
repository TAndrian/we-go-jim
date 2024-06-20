package com.project.we_go_jim.mapper;

import com.project.we_go_jim.dto.CreateUserDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.model.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserDTO> toDTOs(List<UserEntity> userEntities);

    UserDTO toDTO(UserEntity userEntity);

    UserEntity toEntity(CreateUserDTO userDTO);
}
