package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column
    private String content;

    @NotNull
    @Column
    private String username;

    @Column
    private int likeit;

    @Column
    private LocalDateTime createat;

    @Column
    private LocalDateTime updateat;
}
