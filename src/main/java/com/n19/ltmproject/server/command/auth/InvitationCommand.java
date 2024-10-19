package com.n19.ltmproject.server.command.auth;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class InvitationCommand implements Command {
    public InvitationCommand() {
    }
    @Override
    public Response execute(Request request) {
        String usernameToInvite = (String) request.getParams().get("username");

        return Response.builder()
                .status("OK")
                .message("Gui lời moi thanh cong")
                .build();
    }
}
