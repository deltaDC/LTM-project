package com.n19.ltmproject.server.service;

import java.util.HashSet;
import java.util.Set;

public class UserSession {
    public UserSession(){

    }
    private static Set<String> activeUsers = new HashSet<>();

    // Phương thức tĩnh để thêm người dùng
    public static synchronized void addUser(String username) {
        activeUsers.add(username);
        System.out.println("User added: " + username);
    }

    // Phương thức tĩnh để xóa người dùng
    public static synchronized void removeUser(String username) {
        activeUsers.remove(username);
        System.out.println("User removed: " + username);
    }

    // Phương thức tĩnh để lấy danh sách người dùng đang hoạt động
    public static synchronized Set<String> getActiveUsers() {
        return new HashSet<>(activeUsers); // Trả về bản sao của danh sách người dùng đang hoạt động
    }

    // Phương thức tĩnh để thiết lập danh sách người dùng đang hoạt động
    public static void setActiveUsers(Set<String> users) {
        activeUsers.clear();
        activeUsers.addAll(users);
    }

}
