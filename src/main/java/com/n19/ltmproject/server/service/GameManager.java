package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.Leaderboard;
import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.enums.PlayerStatus;

public class GameManager {

    private Leaderboard leaderboard;


    public GameManager() {
        this.leaderboard = new Leaderboard();
    }

    public Game startGame(Player player1, Player player2) {
        player1.setStatus(PlayerStatus.IN_GAME);
        player2.setStatus(PlayerStatus.IN_GAME);
        return new Game(player1, player2);
    }

    public void endGame(Game game) {

    }

    private void updatePlayerStats(Game game) {

    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }
}
