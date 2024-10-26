package com.n19.ltmproject.server.command;

import com.n19.ltmproject.server.command.auth.LoginCommand;
import com.n19.ltmproject.server.command.auth.LogoutCommand;
import com.n19.ltmproject.server.command.auth.SignupCommand;
import com.n19.ltmproject.server.command.game.EndGameById;
import com.n19.ltmproject.server.command.game.GetAllGameDataCommand;
import com.n19.ltmproject.server.command.game.StartNewGame;
import com.n19.ltmproject.server.command.game.UpdateGameScoreCommand;
import com.n19.ltmproject.server.command.player.GetAllPlayerCommand;
import com.n19.ltmproject.server.command.playerHistory.GetAllPlayerHistoryCommand;
import com.n19.ltmproject.server.command.status.*;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.manager.ClientManager;

/**
 * CommandFactory class is a factory class that creates Command objects based on the action string.
 * It is kinda like controller in MVC pattern.
 * It will take the action string from the request and return the appropriate Command object.
 */
public class CommandFactory {

    public static Command getCommand(String action, ClientHandler clientHandle, ClientManager clientManager) {
        return switch (action) {
            case "login" -> new LoginCommand(clientHandle);
            case "signUp" -> new SignupCommand();
            case "getAllGameData" -> new GetAllGameDataCommand();
            case "startNewGame" -> new StartNewGame();
            case "endGameById" -> new EndGameById();
            case "getAllPlayer" -> new GetAllPlayerCommand();
            case "getAllPlayerHistory" -> new GetAllPlayerHistoryCommand();
            case "updateScore" -> new UpdateGameScoreCommand();
            case "logout" -> new LogoutCommand();
            case "invitation" -> new InvitationCommand(clientManager);
            case "refuseInvitation" -> new RefuseInvitationCommand(clientHandle, clientManager);
            case "userJoinedRoom" -> new UserJoinedRoomCommand(clientHandle, clientManager);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}