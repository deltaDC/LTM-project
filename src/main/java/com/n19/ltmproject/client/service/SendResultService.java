package com.n19.ltmproject.client.service;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.message.ReceiveResponseMessage;
import com.n19.ltmproject.client.message.SendRequestMessage;

import java.io.IOException;
import java.util.Map;

public class SendResultService {
    private final SendRequestMessage sendRequestMessage;
    private final ReceiveResponseMessage receiveResponseMessage;

    public SendResultService(ServerHandler serverHandler) {
        this.sendRequestMessage = new SendRequestMessage(serverHandler);
        this.receiveResponseMessage = new ReceiveResponseMessage(serverHandler);
    }

    /**
     * Gửi kết quả trận đấu đến server và nhận phản hồi.
     *
     * @param matchId ID của trận đấu.
     * @param result Kết quả của trận đấu.
     * @return Phản hồi từ server.
     * @throws IOException Nếu có lỗi xảy ra khi nhận phản hồi.
     */
    public String sendMatchResult(String matchId, String result) throws IOException {
        // Tạo params cho yêu cầu
        Map<String, Object> params = Map.of(
                "matchId", matchId,
                "result", result
        );

        // Gửi yêu cầu
        sendRequestMessage.sendRequest("sendMatchResult", params);

        // Nhận phản hồi từ server
        return receiveResponseMessage.receiveResponse().getMessage();
    }
}
