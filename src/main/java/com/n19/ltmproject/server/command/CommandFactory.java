package com.n19.ltmproject.server.command;

import com.n19.ltmproject.server.command.auth.LoginCommand;
import com.n19.ltmproject.server.command.auth.SignupCommand;
import com.n19.ltmproject.server.command.game.EndGameById;
import com.n19.ltmproject.server.command.game.GetAllGameDataCommand;
import com.n19.ltmproject.server.command.game.StartNewGame;
import com.n19.ltmproject.server.command.player.GetAllPlayerCommand;


/**
 * CommandFactory class is a factory class that creates Command objects based on the action string.
 * It is kinda like controller in MVC pattern.
 * It will take the action string from the request and return the appropriate Command object.
 */
public class CommandFactory {

    public static Command getCommand(String action) {
        return switch (action) {
            case "login" -> new LoginCommand();
            case "signUp" -> new SignupCommand();
            case "getAllGameData" -> new GetAllGameDataCommand();
            case "startNewGame" -> new StartNewGame();
            case "endGameById" -> new EndGameById();
            case "getAllPlayer" -> new GetAllPlayerCommand();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
