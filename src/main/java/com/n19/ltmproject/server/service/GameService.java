package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.dao.GameDao;
import com.n19.ltmproject.server.model.Game;

import java.util.List;

public class GameService {

    private final GameDao gameDao;

    public GameService() {
        this.gameDao = new GameDao();
    }

    public List<Game> getAllGameData() {
        return gameDao.getAllGameData();
    }
}
