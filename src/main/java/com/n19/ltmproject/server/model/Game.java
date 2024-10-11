package com.n19.ltmproject.server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gameId;

//    TODO add foreign key constraints if needed
//    @ManyToOne
//    @JoinColumn(name = "player_1_id", insertable = false, updatable = false)
//    private Player player1;

    private long player1Id;

//    @ManyToOne
//    @JoinColumn(name = "player_2_id", insertable = false, updatable = false)
//    private Player player2;

    private long player2Id;

    private long player1Score;

    private long player2Score;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
