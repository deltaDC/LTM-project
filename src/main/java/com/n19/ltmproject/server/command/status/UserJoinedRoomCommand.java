package com.n19.ltmproject.server.command.status;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.dto.Response;
import com.google.gson.Gson;

import java.util.HashMap;

public class UserJoinedRoomCommand implements Command {

    private final ClientHandler clientHandler;
    private final ClientManager clientManager;
    private final Gson gson = new Gson();

    public UserJoinedRoomCommand(ClientHandler clientHandler, ClientManager clientManager) {
        this.clientHandler = clientHandler;
        this.clientManager = clientManager;
    }

    @Override
    public Response execute(Request request) {
        String invitedPlayerName = (String) request.getParams().get("username");
        String inviterName = (String) request.getParams().get("inviterName");

        ClientHandler invitee = clientManager.getClientByUsername(invitedPlayerName);
        ClientHandler inviter = clientManager.getClientByUsername(inviterName);

        if (inviter != null && invitee != null) {
            // Create JSON messages for both inviter and invitee
            String inviteMessage = createJsonMessage("SUCCESS",
                    "[JOINED] User " + invitedPlayerName + " đã tham gia phòng với " + inviterName + ".",
                    null);

            inviter.sendMessage(inviteMessage);
            invitee.sendMessage(inviteMessage);
        }

        return Response.builder()
                .status("SUCCESS")
                .message("Thành công!")
                .data(new HashMap<String, Object>() {{
                    put("username", invitedPlayerName);
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
