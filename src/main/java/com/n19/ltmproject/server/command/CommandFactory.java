package com.n19.ltmproject.server.command;

import com.n19.ltmproject.server.command.auth.LoginCommand;
import com.n19.ltmproject.server.command.auth.SignupCommand;
import com.n19.ltmproject.server.command.game.EndGameById;
import com.n19.ltmproject.server.command.game.GetAllGameDataCommand;
import com.n19.ltmproject.server.command.game.StartNewGame;


/**
 * CommandFactory class is a factory class that creates Command objects based on the action string.
 * It is kinda like controller in MVC pattern.
 * It will take the action string from the request and return the appropriate Command object.
 */
public class CommandFactory {

    public static Command getCommand(String action) {
        switch (action) {
            case "login":
                return new LoginCommand();
            case "signUp":
                return new SignupCommand();
            case "getAllGameData":
                return new GetAllGameDataCommand();
            case "startNewGame":
                return new StartNewGame();
            case "endGameById":
                return new EndGameById();
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }
}
