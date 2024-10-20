package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.service.AuthService;
import com.n19.ltmproject.server.service.UserSession;

import java.util.*;

public class LoginCommand implements Command {
    private final AuthService authService;
    public static UserSession usersession;
    public LoginCommand() {
        this.authService = new AuthService();
    }

    @Override
    public Response execute(Request request) {
        String username = (String) request.getParams().get("username");
        String password = (String) request.getParams().get("password");


        Player player = authService.loginPlayerService(username, password);

        if (player != null) {
            // Thêm người dùng vào UserSession

            usersession.addSession(username);

            // Tạo dữ liệu trả về bao gồm thông tin người dùng
            Map<String, Object> response = new HashMap<>();
//
            response.put("player", player);
            response.put("usersession", usersession);
            // Danh sách người dùng đang hoạt động


            return Response.builder()
                    .status("OK")
                    .message("Login successful")
                    .data(response) // Trả về dữ liệu chứa thông tin người dùng
                    .build();
        } else {
            return Response.builder()
                    .status("FAILED")
                    .message("Invalid username or password")
                    .build();
        }
    }


}

