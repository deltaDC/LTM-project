package com.n19.ltmproject.client.controller;

// CLICK STARTGAME ( XAC NHAN DA READY CHUA)


//  CAN THEM 1 LISTENING TUONG TU MAINPAGE DE LANG NGHE SU KIEN USER2 VAO PHONG
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WaitingRoomController {

    private Stage primaryStage;

    @FXML
    private Label waitingRoomHostName;
    @FXML
    private Label waitingRoomPlayerName;

    public void setUpHost(String waitingRoomHostName){
        this.waitingRoomHostName.setText(waitingRoomHostName);
        this.waitingRoomPlayerName.setText("Waiting");
    }

    public void setUpPlayer(String host, String waitingRoomPlayerName){
        this.waitingRoomHostName.setText(host);
        this.waitingRoomPlayerName.setText(waitingRoomPlayerName);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void ClickExit() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            mainPageController.setupMainPage();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClickStart(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent GamePlayViewParent = loader.load();
        Scene scene = new Scene(GamePlayViewParent);

        GamePlayController gameController = loader.getController();
        gameController.setStage(primaryStage);

        primaryStage.setScene(scene);
    }

}

