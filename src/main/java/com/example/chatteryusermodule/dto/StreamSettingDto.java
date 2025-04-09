package com.example.chatteryusermodule.dto;

import com.example.chatteryusermodule.models.enums.MoodType;
import com.example.chatteryusermodule.models.enums.SentenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StreamSettingDto implements Serializable {
    private String id;
    private String backgroundColor;
    private String textColor;
    private Integer textSize;
    private String font;
    private String aligmentType;
    private Integer delay;
    private MoodType moodType;
    private SentenceType sentenceType;
    private String translationId;
}
