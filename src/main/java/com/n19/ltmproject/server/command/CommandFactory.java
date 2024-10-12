package com.n19.ltmproject.server.command;

import com.n19.ltmproject.server.command.auth.LoginCommand;
import com.n19.ltmproject.server.command.auth.SignupCommand;
import com.n19.ltmproject.server.command.game.GetAllGameDataCommand;

public class CommandFactory {

    public static Command getCommand(String action) {
        switch (action) {
            case "login":
                return new LoginCommand();
            case "signUp":
                return new SignupCommand();
            case "getAllGameData":
                return new GetAllGameDataCommand();
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }
}
