package com.example.chatteryusermodule.dto.creation;

import com.example.chatteryusermodule.models.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTranslationDto implements Serializable {
    private String url;
    private String name;
    private PlatformType platformType;
    private String userId;
}
