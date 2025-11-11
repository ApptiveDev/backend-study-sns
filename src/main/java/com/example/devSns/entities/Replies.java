package com.example.devSns.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

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

    @Column
    @ColumnDefault("0") // Table 생성 시점 기본값
    private int likeit;

    @NotNull
    private String reply;

    @NotNull
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void setLikeit(int likeit) {
        this.likeit = likeit;
    }
}
