package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @JoinColumn(name = "users_id")
    @ManyToOne
    @NotNull
    private Users users;

    @ColumnDefault("0")
    private int likeit;

    @Column
    private LocalDateTime createat;

    @Column
    private LocalDateTime updateat;

    public void setUpdateat(LocalDateTime updateat) {
        this.updateat = updateat;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikeit(int likeit) {
        this.likeit = likeit;
    }
}
