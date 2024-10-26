package com.n19.ltmproject.server.command.status;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.google.gson.Gson;

import java.util.HashMap;

public class RefuseInvitationCommand implements Command {

    private final ClientHandler clientHandler;
    private final ClientManager clientManager;
    private final Gson gson = new Gson(); // For JSON conversion

    public RefuseInvitationCommand(ClientHandler clientHandler, ClientManager clientManager) {
        this.clientHandler = clientHandler;
        this.clientManager = clientManager;
    }

    @Override
    public Response execute(Request request) {
        System.out.println("RefuseInvitationCommand.execute()");

        String invitee = (String) request.getParams().get("invitee");
        String inviter = (String) request.getParams().get("inviter");

        ClientHandler inviterHandler = clientManager.getClientByUsername(inviter);

        if (inviterHandler != null) {
            String jsonMessageToInviter = createJsonMessage("REFUSED",
                    "[REFUSED] User " + invitee + " has declined your invitation.",
                    new HashMap<String, Object>() {{
                        put("invitee", invitee);
                        put("inviter", inviter);
                    }});

            inviterHandler.sendMessage(jsonMessageToInviter);
        }

        return Response.builder()
                .status("OK")
                .message("User " + invitee + " has declined the invitation from " + inviter + ".")
                .data(new HashMap<String, Object>() {{
                    put("inviter", inviter);
                    put("invitee", invitee);
                }})
                .build();
    }

    private String createJsonMessage(String status, String message, Object data) {
        Response response = Response.builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
        return gson.toJson(response);
    }
}