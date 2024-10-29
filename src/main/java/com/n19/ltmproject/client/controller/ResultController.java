package com.n19.ltmproject.client.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ResultController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    @FXML
    private Label resultLabel;

    @FXML
    private Label scoreLabel;

    private boolean isWinner;
    private boolean isDraw;
    private String opponent;
    private long gameId; // Thay đổi để lưu gameId
    private Stage primaryStage;

    @FXML
    private TextField chatInput;

    @FXML
    private VBox chatBox;

    @FXML
    private Label currentPlayerLabel;

    private long currentPlayerId;

    @FXML
    private Label opponentPlayerLabel;

    private long opponentPlayerId;

    private boolean isListening = true;

    // Định dạng thời gian: giờ:phút
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Gson gson = new Gson();

    public void setResults(String results, String score, boolean isWinner, boolean isDraw, String opponent,
            long currentPlayerId, String currentPlayerName, long opponentPlayerId, String opponentPlayerName) {
        this.isWinner = isWinner;
        this.isDraw = isDraw;
        this.opponent = opponent;
        this.gameId = 123; // Gán gameId ở đây (hoặc truyền từ bên ngoài)
        scoreLabel.setText(score);
        resultLabel.setText(isDraw ? "Trận đấu hòa!" : (isWinner ? "Bạn đã thắng!" : "Bạn đã thua!"));

        this.currentPlayerId = currentPlayerId;
        currentPlayerLabel.setText(currentPlayerName);
        this.opponentPlayerId = opponentPlayerId;
        opponentPlayerLabel.setText(opponentPlayerName);
    }

    @FXML
    private void handleExit() {
        isListening = false;
        serverHandler.sendMessage("STOP_LISTENING");
        loadMainPage();
    }

    @FXML
    private void handlePlayAgain() {
        sendResultToServer();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        startListeningToServer();
    }

    private void startListeningToServer() {
        new Thread(() -> {
            try {
                while (isListening) {
                    String serverMessage = serverHandler.receiveMessage();
                    System.out.println("Let's go!!! " + serverMessage);
                    if (serverMessage.contains("Send message from user1:")) {
                        parseServerMessage(serverMessage);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }

    private void parseServerMessage(String serverMessage) {
        // The request is in the format: "currentPlayerId-opponentPlayerId-message"
        String playerIdAndMesStr = serverMessage.substring(25);
        String[] messages = playerIdAndMesStr.split("\\-");
        int senderId = Integer.parseInt(messages[0]);
        int receiverId = Integer.parseInt(messages[1]);
        String message = messages[2];
        System.out.println("message from user2 " + message);
        renderChatMessage(senderId, receiverId, message);
    }

    private void sendResultToServer() {
        boolean isWinner = this.isWinner;
        boolean isDraw = this.isDraw;

        try {
            System.out.println("Dữ liệu gửi về server: " + String
                    .format("EXIT_GAME {\"gameId\": %d, \"isWinner\": %b, \"isDraw\": %b}", gameId, isWinner, isDraw));

            messageService.sendRequestAndReceiveResponse("EXIT_GAME", Map.of(
                    "gameId", gameId,
                    "isWinner", isWinner,
                    "isDraw", isDraw));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không thể gửi kết quả về server");
        }
    }

    private void sendExitNotification() {
        Map<String, Object> params = Map.of("exitResult", opponent);
        Response response = messageService.sendRequestAndReceiveResponse("exitResult", params);
        System.out.println(
                response != null && "OK".equalsIgnoreCase(response.getStatus())
                        ? "Kết quả thoát đã được xác nhận"
                        : "Xác nhận kết quả thoát thất bại");
    }

    private void loadMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            serverHandler.sendMessage("STOP_LISTENING");
            mainPageController.setupMainPage();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to load MainPage.fxml");
        }
    }

    @FXML
    private void handleSendChat() {
        String message = chatInput.getText();
        if (!message.isEmpty()) {
            sendChatToServer(currentPlayerId, opponentPlayerId, message);
            renderChatMessage(this.currentPlayerId, this.opponentPlayerId, message);
        }
    }

    private void sendChatToServer(long currentPlayerId, long opponentPlayerId, String message) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("currentPlayerId", currentPlayerId + "");
        params.put("opponentPlayerId", opponentPlayerId + "");
        params.put("message", message);

        messageService.sendRequestNoResponse("sendChatMessage", params);
    }

    private void renderChatMessage(long currentPlayerId, long opponentPlayerId, String message) {
        // Lấy thời gian hiện tại
        String currentTime = LocalDateTime.now().format(TIME_FORMATTER);

        // Tạo nội dung tin nhắn kèm thời gian
        String messageWithTime;
        if (currentPlayerId == SessionManager.getCurrentUser().getId()) {
            messageWithTime = "[" + currentTime + "] " + this.currentPlayerLabel.getText() + ": " + message;
        } else {
            messageWithTime = "[" + currentTime + "] " + this.opponentPlayerLabel.getText() + ": " + message;
        }

        // Đảm bảo việc cập nhật giao diện xảy ra trên UI thread
        Platform.runLater(() -> {
            // Tạo một Label mới cho tin nhắn
            Label messageLabel = new Label(messageWithTime);
            messageLabel.setMaxWidth(350); // Đặt kích thước tối đa cho tin nhắn
            messageLabel.setWrapText(true); // Cho phép ngắt dòng nếu tin nhắn quá dài

            // Tạo một HBox chứa tin nhắn và căn chỉnh dựa trên người gửi
            HBox messageBox = new HBox(messageLabel);
            messageBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

            if (currentPlayerId == SessionManager.getCurrentUser().getId()) {
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                messageBox.setPadding(new Insets(0, 10, 0, 160)); // Đẩy tin nhắn của mình về sát viền phải
                messageLabel.setStyle(
                        "-fx-text-fill: white; -fx-background-color: #4CAF50; -fx-padding: 10; -fx-background-radius: 10;");
            } else {
                messageBox.setAlignment(Pos.CENTER_LEFT);
                messageBox.setPadding(new Insets(0, 160, 0, 10)); // Đẩy tin nhắn của đối phương về sát viền trái
                messageLabel.setStyle(
                        "-fx-text-fill: white; -fx-background-color: #4A4A4A; -fx-padding: 10; -fx-background-radius: 10;");
            }

            // Thêm tin nhắn vào VBox chatBox
            chatBox.getChildren().add(messageBox);

            // Làm trống ô nhập sau khi gửi tin nhắn
            chatInput.clear();
        });
    }
}
