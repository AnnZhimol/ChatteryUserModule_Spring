package com.example.chatteryusermodule.services.impl;

import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.creation.CreateUserDto;
import com.example.chatteryusermodule.models.User;

public interface UserServiceImpl {
    void add(CreateUserDto entity);
    UserDto getById(String id);
    void edit(UserDto entity);
    User getUserFromDto(UserDto userDto);
}
