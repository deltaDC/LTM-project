package com.n19.ltmproject.server.command.game;

import com.n19.ltmproject.server.command.Command;
import com.n19.ltmproject.server.model.Game;
import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;
import com.n19.ltmproject.server.service.GameService;

import java.util.List;

public class SendMatchResult implements Command {

    public SendMatchResult() {
    }

    @Override
    public Response execute(Request request) {
        return Response.builder()
                .status("OK")
                .message("Gui ket qua thanh cong")
                .build();
    }
}
