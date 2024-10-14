package com.n19.ltmproject.server.dao;

import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.enums.PlayerStatus;
import com.n19.ltmproject.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;

public class AuthDao {
    private SessionFactory sessionFactory;
    public AuthDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Player loginPlayerDao(String username, String password) {
        Session session = sessionFactory.openSession();

        try {
            // Sử dụng CriteriaBuilder để tạo truy vấn
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Player> query = builder.createQuery(Player.class);
            Root<Player> root = query.from(Player.class);

            // Thiết lập các điều kiện cho username và password
            Predicate usernamePredicate = builder.equal(root.get("username"), username);
            Predicate passwordPredicate = builder.equal(root.get("password"), password);

            // Kết hợp cả hai điều kiện trong truy vấn
            query.select(root).where(builder.and(usernamePredicate, passwordPredicate));

            // Thực hiện truy vấn và lấy kết quả duy nhất (nếu có)
            Query q = session.createQuery(query);
            Player player = (Player) q.getSingleResult();
            return player;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public String signUpPlayerDao(String username, String password, String confirmPassword) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        // Kiểm tra password và confirmPassword có khớp nhau không
        if (!password.equals(confirmPassword)) {
            return "Password and confirmPassword do not match.";
        }

        try {
            // Bắt đầu giao dịch
            transaction = session.beginTransaction();

            // Sử dụng CriteriaBuilder để kiểm tra xem username đã tồn tại chưa
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Player> query = builder.createQuery(Player.class);
            Root<Player> root = query.from(Player.class);

            // Tìm người dùng với username đã tồn tại
            Predicate usernamePredicate = builder.equal(root.get("username"), username);
            query.select(root).where(usernamePredicate);

            // Thực hiện truy vấn
            Query q = session.createQuery(query);
            Player existingPlayer = null;

            try {
                existingPlayer = (Player) q.getSingleResult();
            } catch (NoResultException e) {
                // Nếu không tìm thấy người dùng nào, không có vấn đề gì
            }

            // Nếu người dùng đã tồn tại, trả về null hoặc thông báo lỗi
            if (existingPlayer != null) {
                return "Username already exists.";
            }

            // Nếu người dùng chưa tồn tại, tiến hành lưu người dùng mới
            Player newPlayer = new Player();
            newPlayer.setUsername(username);
            newPlayer.setPassword(password);  // Lưu password, có thể mã hóa trước khi lưu nếu cần
            newPlayer.setStatus(PlayerStatus.OFFLINE);  // Set trạng thái ban đầu

            // Lưu người dùng mới vào cơ sở dữ liệu
            session.save(newPlayer);
            transaction.commit();  // Hoàn tất giao dịch

            // Trả về đối tượng người dùng vừa được tạo
            return "Đăng ký thành công!";

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Nếu có lỗi, rollback giao dịch
            }
            e.printStackTrace();
            return "Đăng ký thất bại";
        } finally {
            session.close();  // Đóng session sau khi hoàn tất
        }
    }
}