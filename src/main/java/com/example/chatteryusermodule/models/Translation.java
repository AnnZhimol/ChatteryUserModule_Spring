package com.example.chatteryusermodule.models;

import com.example.chatteryusermodule.models.enums.PlatformType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private PlatformType platformType;

    @OneToOne(mappedBy = "translation", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private StreamSetting streamSetting;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "translation", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<BanUser> bannedUsers;

    @OneToMany(mappedBy = "translation", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<BanWord> bannedWords;
}
