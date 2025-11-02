package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public void setCreateat(LocalDateTime createat) {
        this.createat = createat;
    }

    public void setUpdateat(LocalDateTime updateat) {
        this.updateat = updateat;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
