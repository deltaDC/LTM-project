package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class UpdateGameScoreCommand implements Command {

    private static long player1TempScore = 0;
    private static long player2TempScore = 0;

    private final ClientManager clientManager;

    public UpdateGameScoreCommand(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public Response execute(Request request) {
        // Lấy thông tin từ request
        long gameId = ((Number) request.getParams().get("gameId")).longValue();
        long player1Id = ((Number) request.getParams().get("player1Id")).longValue();
        long player2Id = ((Number) request.getParams().get("player2Id")).longValue();
        long sendingPlayerId = ((Number) request.getParams().get("sendingPlayerId")).longValue();
        long player1Score = ((Number) request.getParams().get("player1Score")).longValue();
        long player2Score = ((Number) request.getParams().get("player2Score")).longValue();

        // Xử lý logic cập nhật điểm tạm thời
        System.out.println("Update Score for Game ID: " + gameId);
        System.out.println("Player1 ID: " + player1Id + " | Score: " + player1Score);
        System.out.println("Player2 ID: " + player2Id + " | Score: " + player2Score);

        player1TempScore = player1Score;
        player2TempScore = player2Score;

        long notifiedPlayerId;
        if (sendingPlayerId == player1Id) {
            notifiedPlayerId = player2Id;
        } else {
            notifiedPlayerId = player1Id;
        }

        sendUpdateToPlayer(notifiedPlayerId, gameId, player1TempScore, player2TempScore);

        return Response.builder()
                .status("OK")
                .message("Score updated successfully and notified the other player.")
                .data("GameId: " + gameId + ", Player1: " + player1TempScore + ", Player2: " + player2TempScore)
                .build();
    }

    private void sendUpdateToPlayer(long playerId, long gameId, long player1Score, long player2Score) {
        // Tìm kiếm ClientHandler tương ứng với playerId
        ClientHandler targetClient = clientManager.getClientByPlayerIdAndUsername(playerId, null);
        if (targetClient != null) {
            String message = String.format("Game ID: %s, Player1 Score: %d, Player2 Score: %d", gameId, player1Score, player2Score);
            targetClient.sendMessage(message);
            System.out.println("Notified Player ID: " + playerId + " with new scores.");
        } else {
            System.out.println("Player ID: " + playerId + " not found.");
        }
    }
}