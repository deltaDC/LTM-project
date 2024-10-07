package com.n19.ltmproject.server.model;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private List<Player> rankedPlayers;

    public void Leaderboard() {
        this.rankedPlayers = new ArrayList<>();
    }

    public void updateLeaderboard(Player player) {
        // Xếp lại vị trí của người chơi trong bảng xếp hạng dựa trên điểm số
    }

    public List<Player> getRankedPlayers() {
        // Trả về danh sách người chơi theo thứ tự tổng điểm
        return rankedPlayers;
    }
}
