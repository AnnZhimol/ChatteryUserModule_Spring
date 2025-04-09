package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.creation.CreateUserDto;
import com.example.chatteryusermodule.services.impl.UserServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUser(@RequestParam @NotNull String userId) {
        UserDto user = userServiceImpl.getById(userId);
        return user == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editUser(@RequestBody UserDto userDto) {
        try {
            userServiceImpl.edit(userDto);
            return new ResponseEntity<>("User successfully edit.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody CreateUserDto createUserDto) {
        try {
            userServiceImpl.add(createUserDto);
            return new ResponseEntity<>("User successfully add.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
