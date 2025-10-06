package entities;

import jakarta.persistence.*;
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

    @Column
    private String content;

    @Column
    private String username;

    @Column
    private int likeit;

    @Column
    private LocalDateTime createat;

    @Column
    private LocalDateTime updateat;
}
