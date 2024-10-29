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
            cq.select(cb.construct(
                            PlayerHistoryDto.class,
                            playerRoot.get("id"),
                            playerRoot.get("username"),
                            playerHistoryRoot.get("draws"),
                            playerHistoryRoot.get("losses"),
                            playerHistoryRoot.get("totalGames"),
                            playerHistoryRoot.get("totalPoints"),
                            playerHistoryRoot.get("wins")
                    )
            ).where(cb.equal(playerRoot.get("id"), playerHistoryRoot.get("playerId")));

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

    // Phương thức cập nhật lịch sử chơi game của người chơi
    public boolean updateGameHistory(long playerId, boolean isWin, boolean isDraw) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean isUpdated = false;

        try {
            transaction = session.beginTransaction();

            // Tìm PlayerHistory theo playerId
            PlayerHistory playerHistory = session.createQuery("FROM PlayerHistory WHERE playerId = :playerId", PlayerHistory.class)
                    .setParameter("playerId", playerId)
                    .uniqueResult();

            if (playerHistory != null) {
                // Cập nhật thông tin thắng, thua, hòa
                if (isWin) {
                    playerHistory.setWins(playerHistory.getWins() + 1);
                } else if (isDraw) {
                    playerHistory.setDraws(playerHistory.getDraws() + 1);
                } else {
                    playerHistory.setLosses(playerHistory.getLosses() + 1);
                }

                // Cập nhật tổng số trận
                playerHistory.setTotalGames(playerHistory.getTotalGames() + 1);
                // Cập nhật tổng điểm (giả sử mỗi trận thắng được 3 điểm, hòa 1 điểm, thua 0 điểm)
                playerHistory.setTotalPoints(playerHistory.getTotalPoints() + (isWin ? 3 : (isDraw ? 1 : 0)));

                // Cập nhật vào cơ sở dữ liệu
                session.update(playerHistory);
                transaction.commit();
                isUpdated = true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return isUpdated;
    }
}