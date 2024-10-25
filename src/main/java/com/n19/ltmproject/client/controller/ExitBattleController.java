// XAC NHAN EXIT
package com.n19.ltmproject.client.controller;

import com.almasb.fxgl.app.GameController;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.service.MessageService;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.animation.Timeline;
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

import java.io.IOException;
import java.util.Map;

public class ExitBattleController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private MessageService messageService = new MessageService(serverHandler);
    private Stage primaryStage;
    private int score;
    private int opponentScore;
    private int timeLeft;
    private Timeline timeline;
    private Timeline exitTimeline;  // Thêm timeline cho trang Exit Battle
    private GamePlayController gamePlayController;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    // Hàm để thiết lập trạng thái của GamePlay
    public void setGamePlayState(int score, int opponentScore, int timeLeft, Timeline timeline,  Stage primaryStage) {
        this.score = score;
        this.opponentScore = opponentScore;
        this.timeLeft = timeLeft;
        this.timeline = timeline;
        this.primaryStage = primaryStage;
    }

    @FXML
    public void setGamePlayController(GamePlayController gamePlayController) {
        this.gamePlayController = gamePlayController;
    }

    @FXML
    private void onConfirmExit() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        serverHandler.sendMessage("NGATLISTENING");
        mainPageController.setup2();
    }

    private void sendResultToServer() {
        boolean isWinner = gamePlayController.getScore() > gamePlayController.getOpponentScore();
        boolean isDraw = gamePlayController.getScore() == gamePlayController.getOpponentScore();

        try {
            long gameId = 123; // Dữ liệu cố định
            System.out.println("Dữ liệu gửi về server: " + String.format("EXIT_GAME {\"gameId\": %d, \"isWinner\": %b, \"isDraw\": %b}", gameId, isWinner, isDraw));

            //TODO change action to EndGameById, and send player history
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            // de cho thread bat null ( bug nho )
            serverHandler.sendMessage("NGATLISTENING");
            mainPageController.setup2();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không thể tải MainPage.fxml");
        }
    }

    @FXML
    private void onCancelExit(ActionEvent event) {
        System.out.println("Thoát đã bị hủy");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
