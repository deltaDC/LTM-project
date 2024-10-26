package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
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

    public void setResults(String results, String score, boolean isWinner, boolean isDraw, String opponent) {
        this.isWinner = isWinner;
        this.isDraw = isDraw;
        this.opponent = opponent;
        this.gameId = 123; // Gán gameId ở đây (hoặc truyền từ bên ngoài)
        scoreLabel.setText(score);
        resultLabel.setText(isDraw ? "Trận đấu hòa!" : (isWinner ? "Bạn đã thắng!" : "Bạn đã thua!"));
    }

    @FXML
    private void handleExit() {
        loadMainPage();
    }

    @FXML
    private void handlePlayAgain() {
        sendResultToServer();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void sendResultToServer() {
        boolean isWinner = this.isWinner;
        boolean isDraw = this.isDraw;

        try {
            System.out.println("Dữ liệu gửi về server: " + String.format("EXIT_GAME {\"gameId\": %d, \"isWinner\": %b, \"isDraw\": %b}", gameId, isWinner, isDraw));

            messageService.sendRequest("EXIT_GAME", Map.of(
                    "gameId", gameId,
                    "isWinner", isWinner,
                    "isDraw", isDraw
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không thể gửi kết quả về server");
        }
    }

    private void sendExitNotification() {
        Map<String, Object> params = Map.of("exitResult", opponent);
        Response response = messageService.sendRequest("exitResult", params);
        System.out.println(
                response != null && "OK".equalsIgnoreCase(response.getStatus())
                ? "Kết quả thoát đã được xác nhận"
                : "Xác nhận kết quả thoát thất bại"
        );
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
}
