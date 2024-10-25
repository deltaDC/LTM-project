package com.n19.ltmproject.server.model.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class PlayerHistoryDto {

    private long id;
    private String username;
    private int totalGames;
    private int wins;
    private int losses;
    private int draws;
    private int totalPoints;
}
