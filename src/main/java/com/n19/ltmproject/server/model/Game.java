package com.n19.ltmproject.server.model;

import java.time.LocalDateTime;
import java.util.List;

public class Game {

    private int gameId;
    private Player player1;
    private Player player2;
    private int player1Score;
    private int player2Score;
    private Player winner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Game() {}

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.startTime = LocalDateTime.now();
    }

    public Game(int gameId, Player player1, Player player2, int player1Score, int player2Score, Player winner, LocalDateTime startTime, LocalDateTime endTime) {
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.winner = winner;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", player1Score=" + player1Score +
                ", player2Score=" + player2Score +
                ", winner=" + winner +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
