package com.n19.ltmproject.server.dao;

import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.PlayerHistory;
import com.n19.ltmproject.server.model.dto.PlayerHistoryDto;
import com.n19.ltmproject.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PlayerHistoryDao {
    private SessionFactory sessionFactory;

    public PlayerHistoryDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<PlayerHistoryDto> getAllPlayerHistory() {

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<PlayerHistoryDto> playerHistoryDtos = new ArrayList<>();

        try {
            transaction = session.beginTransaction();

            // Create CriteriaBuilder
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Create CriteriaQuery for PlayerHistoryDto
            CriteriaQuery<PlayerHistoryDto> cq = cb.createQuery(PlayerHistoryDto.class);

            // Define root entities
            Root<Player> playerRoot = cq.from(Player.class);
            Root<PlayerHistory> playerHistoryRoot = cq.from(PlayerHistory.class);

            // Define join condition on playerId
            cq.select(cb.construct
                        (
                            PlayerHistoryDto.class,
                            playerRoot.get("id"),
                            playerRoot.get("username"),
                            playerHistoryRoot.get("draws"),
                            playerHistoryRoot.get("losses"),
                            playerHistoryRoot.get("totalGames"),
                            playerHistoryRoot.get("totalPoints"),
                            playerHistoryRoot.get("wins")
                        )
                    )
                    .where(cb.equal(playerRoot.get("id"), playerHistoryRoot.get("playerId")));

            // Execute the query and get results
            playerHistoryDtos = session.createQuery(cq).getResultList();

            // Sort by total points in descending order
            playerHistoryDtos.sort((p1, p2) -> Integer.compare(p2.getTotalPoints(), p1.getTotalPoints()));

            transaction.commit();
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
