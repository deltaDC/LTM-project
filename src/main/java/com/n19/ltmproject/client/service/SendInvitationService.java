package com.n19.ltmproject.client.service;



import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.service.message.ReceiveResponseMessage;
import com.n19.ltmproject.client.service.message.SendRequestMessage;

import java.io.IOException;
import java.util.Map;

public class SendInvitationService {
    private final SendRequestMessage sendRequestMessage;
    private final ReceiveResponseMessage receiveResponseMessage;

    public SendInvitationService(ServerHandler serverHandler) {
        this.sendRequestMessage = new SendRequestMessage(serverHandler);
        this.receiveResponseMessage = new ReceiveResponseMessage(serverHandler);
    }

    /**
     * Gửi kết quả trận đấu đến server và nhận phản hồi.
     *
     * @param userId ID của đối thủ
     * @return Phản hồi từ server.
     * @throws IOException Nếu có lỗi xảy ra khi nhận phản hồi.
     */
    public String sendInvitation(String username) throws IOException {
        // Tạo params cho yêu cầu
        Map<String, Object> params = Map.of(
                "userId", username

        );

        // Gửi yêu cầu
        sendRequestMessage.sendRequest("sendInvitation", params);

        // Nhận phản hồi từ server
        return receiveResponseMessage.receiveResponse().getMessage();
    }
}

