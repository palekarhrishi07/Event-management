package com.event_management.event_management_system_backend.mapper;

import com.event_management.event_management_system_backend.Dto.AdminDto;
import com.event_management.event_management_system_backend.Dto.SignUpDto;
import com.event_management.event_management_system_backend.Dto.UserDto;
import com.event_management.event_management_system_backend.model.admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Mapper(componentModel= "spring")

public interface AdminMapper {
    AdminDto toAdminDto(admin admin);

    @Mapping(target = "password", ignore = true)
    admin signUpToAdmin(SignUpDto signUpDto);
}
