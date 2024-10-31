package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.model.enums.PlayerStatus;
import com.n19.ltmproject.server.service.GameService;
import com.n19.ltmproject.server.service.PlayerService;

public class EndGameById implements Command {

    private final GameService gameService;
    private final PlayerService playerService;

    public EndGameById() {
        this.gameService = new GameService();
        this.playerService = new PlayerService();
    }

    @Override
    public Response execute(Request request) {
        long gameId = ((Number) request.getParams().get("gameId")).longValue();
        long player1Id = ((Number) request.getParams().get("player1Id")).longValue();
        long player2Id = ((Number) request.getParams().get("player2Id")).longValue();
        long player1Score = ((Number) request.getParams().get("player1Score")).longValue();
        long player2Score = ((Number) request.getParams().get("player2Score")).longValue();

        Game game = gameService.endGameById(gameId, player1Id, player2Id, player1Score, player2Score);
        playerService.updatePlayerStatusById(game.getPlayer1Id(), PlayerStatus.ONLINE);
        playerService.updatePlayerStatusById(game.getPlayer2Id(), PlayerStatus.ONLINE);

        return Response.builder()
                .status("OK")
                .message("Game ended successfully")
                .data(game)
                .build();
    }
}
