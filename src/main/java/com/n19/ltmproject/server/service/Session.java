package com.n19.ltmproject.server.service;

public class Session {
    private static String userID;
    private static String username;
    public Session(){

    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        Session.userID = userID;
    }
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Session.username = username;
    }
}
