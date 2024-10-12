package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.model.enums.PlayerStatus;
import com.n19.ltmproject.server.dao.PlayerDao;

public class PlayerService {

    private final PlayerDao playerDao;

    public PlayerService() {
        this.playerDao = new PlayerDao();
    }

    public void updatePlayerStatusById(long player1Id, PlayerStatus playerStatus) {
        playerDao.updatePlayerStatusById(player1Id, playerStatus);
    }
}
