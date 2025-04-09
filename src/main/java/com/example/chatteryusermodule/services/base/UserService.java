package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.creation.CreateUserDto;
import com.example.chatteryusermodule.models.User;
import com.example.chatteryusermodule.repositories.UserRepository;
import com.example.chatteryusermodule.services.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.chatteryusermodule.utils.CipherUtil.decryptId;
import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;

@Service
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getUserFromCreateDto(CreateUserDto createUserDto) {
        return User.builder()
                .nickname(createUserDto.getNickname())
                .password(createUserDto.getPassword())
                .email(createUserDto.getEmail())
                .role(createUserDto.getRole())
                .build();
    }

    public CreateUserDto getUserCreateFromOrigin(User user) {
        return CreateUserDto.builder()
                .nickname(user.getNickname())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserDto getUserDtoFromOrigin(User user) {
        return UserDto.builder()
                .id(encryptId(user.getId()))
                .nickname(user.getNickname())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public User getUserFromDto(UserDto userDto) {
        return User.builder()
                .id(decryptId(userDto.getId()))
                .nickname(userDto.getNickname())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }

    public void save(User entity) {
        userRepository.save(entity);
        log.info("User with id = {} successfully saved", entity.getId());
    }

    @Override
    public void add(CreateUserDto entity) {
        if (userRepository.existsByEmail(entity.getEmail())) {
            log.error("User already exist");
            throw new IllegalArgumentException("The user is already exist.");
        }

        User newUser = userRepository.save(getUserFromCreateDto(entity));
        log.info("User with id = {} ({}) successfully added", newUser.getId(), encryptId(newUser.getId()));
    }

    @Override
    public UserDto getById(String id) {
        Long longId = decryptId(id);
        User foundUser = userRepository.findById(longId).orElse(null);

        if (foundUser != null) {
            log.info("User with id = {} ({}) successfully found", longId, encryptId(longId));
            return getUserDtoFromOrigin(foundUser);
        }

        log.error("Search User with id = {} ({}) failed", longId, encryptId(longId));
        return null;
    }

    @Override
    public void edit(UserDto entity) {
        UserDto currentUser = getById(entity.getId());
        long longId = decryptId(entity.getId());

        if (currentUser == null) {
            log.error("User with id = {} ({}) not found", longId, encryptId(longId));
            throw new EntityNotFoundException();
        }

        currentUser.setNickname(entity.getNickname() == null ? currentUser.getNickname() : entity.getNickname());
        currentUser.setEmail(entity.getEmail() == null ? currentUser.getEmail() : entity.getEmail());

        userRepository.save(getUserFromDto(currentUser));
        log.info("User with id = {} ({}) successfully edit", longId, encryptId(longId));
    }

    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            log.info("User with email = {} successfully found", email);
            return user;
        }

        log.error("Search User with email = {} failed", email);
        return null;
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }
}
