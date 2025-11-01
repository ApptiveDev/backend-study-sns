package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Replies {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "posts_id")
    @ManyToOne
    @NotNull
    private Posts posts;

    @NotNull
    private String username;

    @NotNull
    private String reply;
}
