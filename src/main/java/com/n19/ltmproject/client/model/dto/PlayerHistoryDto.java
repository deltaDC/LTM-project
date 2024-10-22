package com.n19.ltmproject.client.model.dto;

import lombok.*;

@Data
public class PlayerHistoryDto {
    private long id;
    private String username;
    private int totalGames;
    private int wins;
    private int losses;
    private int draws;
    private int totalPoints;

    public PlayerHistoryDto(long id, String username, int totalGames, int wins, int losses, int draws, int totalPoints) {
        this.id = id;
        this.username = username;
        this.totalGames = totalGames;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.totalPoints = totalPoints;
    }
}
