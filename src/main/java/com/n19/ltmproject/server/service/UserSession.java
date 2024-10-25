package com.n19.ltmproject.server.service;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private static Map<String, Session> sessions = new HashMap<>();

    // Thêm một phiên người dùng vào danh sách
    public static void addSession( String username) {
        Session session = new Session(username);
        sessions.put(username, session);
    }

    // Lấy phiên người dùng dựa trên userID
    public static Session getSession(String username) {
        return sessions.get(username);
    }

    // Xóa một phiên người dùng khi họ đăng xuất
    public static void removeSession(String username) {
        sessions.remove(username);
    }

    // Kiểm tra xem user có đang trong phiên không
    public static boolean isUserLoggedIn(String username) {
        return sessions.containsKey(username);
    }
    public static void setSession(UserSession otherSession) {
        // Clear existing sessions
        sessions.clear();

        // Copy sessions from the other UserSession
        for (Map.Entry<String, Session> entry : otherSession.getAllSessions().entrySet()) {
            sessions.put(entry.getKey(), entry.getValue());
        }
    }

    // Lấy tất cả các phiên hiện tại
    public static Map<String, Session> getAllSessions() {
        return sessions;
    }
}
