package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.service.AuthService;

import java.util.*;

public class LoginCommand implements Command {
    private final AuthService authService;
    private final ClientHandler clientHandler; // Tham chiếu đến ClientHandler

    public LoginCommand(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.authService = new AuthService();
    }

    @Override
    public Response execute(Request request) {
        //TODO implement login command
//        System.out.println(request.toString());
        System.out.println("LoginCommand.execute() called");

        String username = (String) request.getParams().get("username");
        String password = (String) request.getParams().get("password");


        Player player = authService.loginPlayerService(username, password);

        if (player != null) {
            // Nếu đăng nhập thành công, trả về đối tượng Player trong response
            clientHandler.setUsername(username);
            return Response.builder()
                    .status("OK")
                    .message("Login successful")
                    .data(player)
                    .build();
        } else {
            // Nếu đăng nhập thất bại, trả về thông báo lỗi
            return Response.builder()
                    .status("FAILED")
                    .message("Invalid username or password")
                    .build();
        }
    }
}