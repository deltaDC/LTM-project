package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public class InvitationCommand implements Command {

    public InvitationCommand() {}

    @Override
    public Response execute(Request request) {

        System.out.println("InvitationCommand.execute()");

        String inviter = (String) request.getParams().get("inviter");
        String invitee = (String) request.getParams().get("invitee");

        return Response.builder()
                .status("OK")
                .message(inviter +" Mời " + invitee + " Thành Công")
                .build();
    }
}
