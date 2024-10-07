package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.enums.PlayerStatus;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private Map<String, Player> players = new HashMap<>();

    public Player register(String username, String password, String email) {
        if (players.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        Player player = new Player(username, password, email);
        players.put(username, player);
        return player;
    }

    public Player login(String username, String password) {
        Player player = players.get(username);
        if (player == null || !player.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        player.setStatus(PlayerStatus.ONLINE);
        return player;
    }

    public void logout(Player player) {
        player.setStatus(PlayerStatus.OFFLINE);
    }

    public Player getPlayer(String username) {
        return players.get(username);
    }

    public Map<String, Player> getOnlinePlayers() {
        Map<String, Player> onlinePlayers = new HashMap<>();
        for (Player player : players.values()) {
            if (player.getStatus() == PlayerStatus.ONLINE) {
                onlinePlayers.put(player.getUsername(), player);
            }
        }
        return onlinePlayers;
    }
}
