package com.example.chatteryusermodule.dto.creation;

import com.example.chatteryusermodule.models.enums.MoodType;
import com.example.chatteryusermodule.models.enums.SentenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStreamSettingDto implements Serializable {
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
