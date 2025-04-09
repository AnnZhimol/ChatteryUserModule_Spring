package com.example.chatteryusermodule.models;

import com.example.chatteryusermodule.models.enums.StateVerification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private StateVerification state;
    @Column(nullable = false)
    private LocalDateTime deadline;
    @Column(nullable = false)
    private LocalDateTime createDate;
    @Column(nullable = false)
    private String hash;

    @OneToOne(mappedBy = "userVerification", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private User user;
}
