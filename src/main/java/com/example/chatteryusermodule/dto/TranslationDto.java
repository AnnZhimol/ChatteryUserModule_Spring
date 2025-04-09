package com.example.chatteryusermodule.dto;

import com.example.chatteryusermodule.models.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TranslationDto implements Serializable {
    private String id;
    private String url;
    private String name;
    private PlatformType platformType;
}
