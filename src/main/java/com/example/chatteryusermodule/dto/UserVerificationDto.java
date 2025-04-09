package com.example.chatteryusermodule.dto;

import com.example.chatteryusermodule.models.enums.StateVerification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserVerificationDto implements Serializable {
    private String id;
    private StateVerification state;
    private LocalDateTime deadline;
    private LocalDateTime createDate;
    private String hash;
}
