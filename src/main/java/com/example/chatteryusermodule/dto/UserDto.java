package com.example.chatteryusermodule.dto;

import com.example.chatteryusermodule.models.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto implements Serializable {
    private String id;
    @Email
    private String email;
    @Size(min = 10)
    private String password;
    private String nickname;
    private UserRole role;
}
