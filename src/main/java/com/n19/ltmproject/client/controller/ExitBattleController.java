// XAC NHAN EXIT
package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

public class ExitBattleController {

    private ServerHandler serverHandler;
    private Stage primaryStage;
    private int score;
    private int opponentScore;
    private int timeLeft;
    private Timeline timeline;
    private Timeline exitTimeline;  // Thêm timeline cho trang Exit Battle

    // Hàm để thiết lập trạng thái của GamePlay
    public void setGamePlayState(int score, int opponentScore, int timeLeft, Timeline timeline, ServerHandler serverHandler, Stage primaryStage) {
        this.score = score;
        this.opponentScore = opponentScore;
        this.timeLeft = timeLeft;
        this.timeline = timeline;
        this.serverHandler = serverHandler;
        this.primaryStage = primaryStage;

        startExitCountdown(); // Bắt đầu đếm ngược trên trang Exit Battle
    }

    // Hàm đếm ngược trên trang Exit Battle
    private void startExitCountdown() {
        exitTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timeLeft > 0) {
                timeLeft--;
            }
        }));
        exitTimeline.setCycleCount(Timeline.INDEFINITE);
        exitTimeline.play();
    }

    // Xử lý khi click nút Confirm
    public void ClickConfirm(ActionEvent e) throws IOException {
        // Dừng cả hai timeline khi nhấn Confirm
        if (timeline != null) {
            timeline.stop();
        }
        if (exitTimeline != null) {
            exitTimeline.stop();
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent mainPageViewParent = loader.load();
        Scene scene = new Scene(mainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setServerConnection(serverHandler, primaryStage);

        primaryStage.setScene(scene);
    }

    // Xử lý khi click nút Cancel
    public void ClickCancel(ActionEvent e) throws IOException {
        if (exitTimeline != null) {
            exitTimeline.stop();  // Dừng bộ đếm ngược của trang Exit Battle
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent gamePlayViewParent = loader.load();
        Scene scene = new Scene(gamePlayViewParent);

        GamePlayController gamePlayController = loader.getController();
        gamePlayController.setServerConnection(serverHandler, primaryStage);

        // Ghi đè thời gian còn lại của GamePlay bằng thời gian từ trang Exit Battle
        gamePlayController.setTimeLeft(timeLeft);  // Sử dụng hàm mới setTimeLeft để cập nhật thời gian

        // Cập nhật lại timer cho trang GamePlay
//        gamePlayController.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> gamePlayController.updateTimer()));
        gamePlayController.timeline.setCycleCount(Timeline.INDEFINITE);
        gamePlayController.timeline.play();

        primaryStage.setScene(scene);
    }

}
