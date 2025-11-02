package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Replies {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "posts_id")
    @ManyToOne
    @NotNull
    private Posts posts;

    @JoinColumn(name = "users_id")
    @ManyToOne
    @NotNull
    private Users users;

    @NotNull
    private String reply;
}
