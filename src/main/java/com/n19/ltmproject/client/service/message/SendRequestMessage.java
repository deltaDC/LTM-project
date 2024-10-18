package com.n19.ltmproject.client.service.message;

import com.google.gson.Gson;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Request;

import java.util.Map;

public class SendRequestMessage {

    private final ServerHandler serverHandler;
    private final Gson gson = new Gson();

    public SendRequestMessage(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void sendRequest(String action, Map<String, Object> params) {
        // Tạo đối tượng Request
        Request request = new Request();
        request.setAction(action);
        request.setParams(params);

        // Chuyển đổi Request thành JSON và gửi đi
        String jsonRequest = gson.toJson(request);
        serverHandler.sendMessage(jsonRequest);
    }
}