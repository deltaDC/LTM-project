package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.model.enums.PlayerStatus;
import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.service.GameService;
import com.n19.ltmproject.server.service.PlayerService;

public class StartNewGame implements Command {

    private final GameService gameService;
    private final PlayerService playerService;

    public StartNewGame() {
        this.playerService = new PlayerService();
        this.gameService = new GameService();
    }

    @Override
    public Response execute(Request request) {
        System.out.println(request.toString());

        long player1Id = ((Number) request.getParams().get("player1Id")).longValue();
        long player2Id = ((Number) request.getParams().get("player2Id")).longValue();

        Game game = gameService.createNewGame(player1Id, player2Id);

        playerService.updatePlayerStatusById(player1Id, PlayerStatus.IN_GAME);
        playerService.updatePlayerStatusById(player2Id, PlayerStatus.IN_GAME);

        return Response.builder()
                .status("OK")
                .message("New game started successfully")
                .data(game)
                .build();
    }
}
