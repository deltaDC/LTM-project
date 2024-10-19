package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.command.auth.LoginCommand;

import java.util.HashMap;
import java.util.Map;

public class LogoutCommand implements Command {
    public LogoutCommand() {
//        this.authService = new AuthService();
    }
    @Override
    public Response execute(Request request) {
        String username = (String) request.getParams().get("username");


        System.out.println(username + " logged out");
        LoginCommand.usersession.removeUser(username);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("activeUsers", LoginCommand.usersession.getActiveUsers()); // Danh sách người dùng đang hoạt động


        return Response.builder()
                .status("OK")
                .message("Logout successful")
                .data(responseData) // Trả về dữ liệu chứa thông tin người dùng
                .build();
    }
}
