package com.n19.ltmproject.server.dao;

import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
}
