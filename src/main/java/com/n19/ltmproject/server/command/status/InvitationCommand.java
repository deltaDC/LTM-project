package com.n19.ltmproject.server.command.status;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.handler.ClientHandler;
import com.n19.ltmproject.server.manager.ClientManager;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

import java.util.HashMap;

public class InvitationCommand implements Command {

    private final ClientManager clientManager;

    public InvitationCommand(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public Response execute(Request request) {
        System.out.println("InvitationCommand.execute()");

        String inviter = (String) request.getParams().get("inviter");
        String invitee = (String) request.getParams().get("invitee");
        long inviterId = ((Number) request.getParams().get("inviterId")).longValue();
        long inviteeId = ((Number) request.getParams().get("inviteeId")).longValue();

        ClientHandler inviterHandler = clientManager.getClientByPlayerIdAndUsername(inviterId, inviter);
        ClientHandler inviteeHandler = clientManager.getClientByPlayerIdAndUsername(inviteeId, invitee);

        if (inviteeHandler != null) {
            inviteeHandler.sendMessage("[INVITATION] " + inviter + " với playerId " + inviterId + " đã mời bạn tham gia thi đấu.");
        }
        else System.out.println(inviteeHandler);

        if (inviterHandler != null) {
            inviterHandler.sendMessage("Bạn đã gửi lời mời đến " + invitee + " với playerId " + inviteeId + " .");
        }
        else System.out.println(inviterHandler);

        return Response.builder()
                .status("OK")
                .message("Lời mời từ " + inviter + " đến " + invitee + " đã được gửi thành công.")
                .data(new HashMap<String, Object>() {{
                    put("inviter", inviter);
                    put("inviterId", inviterId);
                    put("invitee", invitee);
                    put("inviteeId", inviteeId);
                }})
                .build();
    }
}