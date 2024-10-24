package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class UpdateGameScoreCommand implements Command {

    private static long player1TempScore = 0;
    private static long player2TempScore = 0;

    @Override
    public Response execute(Request request) {
        // Lấy thông tin điểm số từ request
        long player1Score = ((Number) request.getParams().get("player1Score")).longValue();
        long player2Score = ((Number) request.getParams().get("player2Score")).longValue();

        // Xử lý logic cập nhật điểm tạm thời, ví dụ gửi thông báo về cho client khác.
        System.out.println("Update Score: Player1 = " + player1Score + ", Player2 = " + player2Score);

        player1TempScore = player1Score;
        player2TempScore = player2Score;

        return Response.builder()
                .status("OK")
                .message("Score updated successfully")
                .data("Player1: " + player1TempScore + ", Player2: " + player2TempScore)
                .build();
    }
}
