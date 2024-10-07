package com.n19.ltmproject.server.model;

import com.n19.ltmproject.server.model.enums.PlayerStatus;

public class Player {

    private int id;
    private String username;
    private String password;
    private String email;
    private int totalPoints;
    private int totalGames;
    private int wins;
    private int losses;
    private PlayerStatus status;

    public Player() {}

    public Player(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = PlayerStatus.OFFLINE;
    }

    public Player(int id, String username, String password, String email, int totalPoints, int totalGames, int wins, int losses, PlayerStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.totalPoints = totalPoints;
        this.totalGames = totalGames;
        this.wins = wins;
        this.losses = losses;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", totalPoints=" + totalPoints +
                ", totalGames=" + totalGames +
                ", wins=" + wins +
                ", losses=" + losses +
                ", status=" + status +
                '}';
    }
}
