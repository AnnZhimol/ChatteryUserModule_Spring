package com.example.chatteryusermodule.models;

import com.example.chatteryusermodule.models.enums.MoodType;
import com.example.chatteryusermodule.models.enums.SentenceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class StreamSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String backgroundColor;
    private String textColor;
    private Integer textSize;
    private String font;
    private String aligmentType;
    private Integer delay;
    private MoodType moodType;
    private SentenceType sentenceType;

    @OneToOne(fetch = FetchType.EAGER)
    private Translation translation;
}
