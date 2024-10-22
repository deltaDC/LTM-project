package com.n19.ltmproject.server.dao;

import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.PlayerHistory;
import com.n19.ltmproject.server.model.dto.PlayerHistoryDto;
import com.n19.ltmproject.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PlayerHistoryDao {
    private SessionFactory sessionFactory;

    public PlayerHistoryDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<PlayerHistoryDto> getAllPlayerHistory() {
        List<PlayerHistory> playerHistories = null;
        List<Player> players = null;
        List<PlayerHistoryDto> playerHistoryDtos = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            playerHistories = session.createQuery("from PlayerHistory", PlayerHistory.class).list();
            players = session.createQuery("from Player", Player.class).list();
            transaction.commit();
            for (Player player : players) {
                for (PlayerHistory playerHistory : playerHistories) {
                    if (player.getId() == playerHistory.getPlayerId()) {
                        PlayerHistoryDto playerHistoryDto = new PlayerHistoryDto();
                        playerHistoryDto.setId(player.getId());
                        playerHistoryDto.setUsername(player.getUsername());
                        playerHistoryDto.setDraws(playerHistoryDto.getDraws());
                        playerHistoryDto.setLosses(playerHistoryDto.getLosses());
                        playerHistoryDto.setTotalGames(playerHistoryDto.getTotalGames());
                        playerHistoryDto.setTotalPoints(playerHistoryDto.getTotalPoints());
                        playerHistoryDto.setWins(playerHistoryDto.getWins());
                        playerHistoryDtos.add(playerHistoryDto);
                    }
                }
            }
            playerHistoryDtos.sort((p1, p2) -> Integer.compare(p2.getTotalPoints(), p1.getTotalPoints()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return playerHistoryDtos;
    }
}
