package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.UserDto;
import com.Library.restAPI.model.User;

public class UserMapper {

    public static UserDto toDto(User entity){
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .name(entity.getName())
                .surname(entity.getSurname())
                .address(entity.getAddress())
                .city(entity.getCity())
                .role(entity.getRole())
                .build();
    }
}
