package com.example.chatteryusermodule.dto.creation;

import com.example.chatteryusermodule.models.enums.UserRole;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto implements Serializable {
    private String email;
    @Size(min = 10)
    private String password;
    private String nickname;
    private UserRole role;
}
