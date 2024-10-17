package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.service.GameService;

import java.util.List;

public class SendInvitationCommand implements Command {

    public SendInvitationCommand() {
    }

    @Override
    public Response execute(Request request) {
        return Response.builder()
                .status("OK")
                .message("Gui loi moi thanh cong")
                .build();
    }
}