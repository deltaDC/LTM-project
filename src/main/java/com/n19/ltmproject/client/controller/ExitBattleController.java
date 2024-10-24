// XAC NHAN EXIT
package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.service.MessageService;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.util.Map;

public class ExitBattleController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private int score;
    private int opponentScore;
    private int timeLeft;
    private Timeline timeline;
    private Timeline exitTimeline;  // Thêm timeline cho trang Exit Battle
//    private UserSession usersessions;
//    private Session session;
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
//        this.session = session;
//        this.usersessions = usersessions;
    }
    // Hàm để thiết lập trạng thái của GamePlay
    public void setGamePlayState(int score, int opponentScore, int timeLeft, Timeline timeline,  Stage primaryStage) {
        this.score = score;
        this.opponentScore = opponentScore;
        this.timeLeft = timeLeft;
        this.timeline = timeline;
        this.primaryStage = primaryStage;

        startExitCountdown(); // Bắt đầu đếm ngược trên trang Exit Battle
    }

    @FXML
    public void setGamePlayController(GamePlayController gamePlayController) {
        this.gamePlayController = gamePlayController;
    }

    @FXML
    private void onConfirmExit() {
        sendResultToServer();
        goToHomePage(null);
    }

    private void sendResultToServer() {
        boolean isWinner = gamePlayController.getScore() > gamePlayController.getOpponentScore();
        boolean isDraw = gamePlayController.getScore() == gamePlayController.getOpponentScore();

        try {
            long gameId = 123; // Dữ liệu cố định
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

    private void goToHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
            Parent mainPageView = loader.load();
            Scene scene = new Scene(mainPageView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không thể tải MainPage.fxml");
        }
    }

    @FXML
    private void onCancelExit(ActionEvent event) {
        System.out.println("Thoát đã bị hủy");
        // Đóng modal để quay lại trang GamePlay
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
