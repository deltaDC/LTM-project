package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class LoginCommand implements Command {

    @Override
    public Response execute(Request request) {
        //TODO implement login command

        System.out.println("LoginCommand.execute() called");
        System.out.println("Action: " + request.getAction());
        System.out.println("Data: " + request.getData());

        Response response = new Response();
        response.setStatus("OK");
        response.setMessage("Login successful");
        response.setData(null);

        return response;
    }
}
