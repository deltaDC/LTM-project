package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class LoginCommand implements Command {

    @Override
    public Response execute(Request request) {
        //TODO implement login command

        System.out.println("LoginCommand.execute() called");

        return Response.builder()
                .status("OK")
                .message("Login successful")
                .build();
    }
}
