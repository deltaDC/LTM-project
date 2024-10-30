package com.n19.ltmproject.server.dao;

import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class GameDao {

    private SessionFactory sessionFactory;

    public GameDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Game> getAllGameData() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Game> games = null;

        try {
            transaction = session.beginTransaction();
            games = session.createQuery("FROM Game", Game.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return games;
    }

    public Game createNewGame(long player1Id, long player2Id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Game game = null;

        try {
            transaction = session.beginTransaction();
            game = new Game();
            game.setPlayer1Id(player1Id);
            game.setPlayer2Id(player2Id);
//            game.setStartTime(String.valueOf(new Date()));
            game.setStartTime(String.valueOf(new Timestamp(System.currentTimeMillis())));
            session.save(game);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return game;
    }

    public Game endGameById(long gameId, long player1Score, long player2Score) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Game game = null;

        try {
            transaction = session.beginTransaction();
            game = session.get(Game.class, gameId);
            game.setPlayer1Score(player1Score);
            game.setPlayer2Score(player2Score);
            game.setEndTime(String.valueOf(new Timestamp(System.currentTimeMillis())));
            session.update(game);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return game;
    }
}
