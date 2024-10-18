package com.n19.ltmproject.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultController {

    @FXML
    private Label resultLabel;

    @FXML
    private Label scoreLabel;

    private boolean isWinner;
    private boolean isDraw;
    private String opponent;

    public void setResults(String results, String score, boolean isWinner, boolean isDraw, String opponent) {
        this.isWinner = isWinner;
        this.isDraw = isDraw;
        this.opponent = opponent;

        scoreLabel.setText(score);

        if (isDraw) {
            resultLabel.setText("Trận đấu hòa!");
        } else if (isWinner) {
            resultLabel.setText("Bạn đã thắng!");
        } else {
            resultLabel.setText("Bạn đã thua!");
        }
    }

    @FXML
    private void handleExit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
            Parent mainPage = loader.load();
            Scene scene = new Scene(mainPage);

            Stage stage = (Stage) scoreLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to load MainPage.fxml");
        }
    }

    @FXML
    private void handlePlayAgain() {
        sendRematchInvitation(opponent);

        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.close();
    }

    private void sendRematchInvitation(String opponent) {
        System.out.println("Sending rematch invitation to " + opponent);
    }
}
