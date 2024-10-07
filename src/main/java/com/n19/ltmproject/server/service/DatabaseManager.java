package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.Player;

import java.sql.Connection;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(Connection connection) {
        this.connection = connection;
    }

    public void savePlayer(Player player) {
    }

    public void saveGame(Game game) {
    }

    public Player getPlayerById(int id) {
        return null;
    }
}
