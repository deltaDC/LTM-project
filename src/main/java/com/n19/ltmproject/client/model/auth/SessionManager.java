package com.n19.ltmproject.client.model.auth;

import com.n19.ltmproject.client.model.Player;

public class SessionManager {
    private static Player currentUser;

    // Lấy thông tin người dùng hiện tại
    public static Player getCurrentUser() {
        return currentUser;
    }

    // Đặt thông tin người dùng hiện tại
    public static void setCurrentUser(Player user) {
        currentUser = user;
    }

    // Xóa thông tin người dùng (đăng xuất)
    public static void clearSession() {
        currentUser = null;
    }
}
