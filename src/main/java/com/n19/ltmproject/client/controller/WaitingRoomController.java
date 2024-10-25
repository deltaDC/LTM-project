package com.n19.ltmproject.client.controller;

// CLICK STARTGAME ( XAC NHAN DA READY CHUA)


//  CAN THEM 1 LISTENING TUONG TU MAINPAGE DE LANG NGHE SU KIEN USER2 VAO PHONG
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.n19.ltmproject.client.handler.ServerHandler;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private volatile boolean running = true;

    //TODO rename for better readability
    @FXML
    private Label waitingroomnamechuphong;
    @FXML
    private Label waitingroomnamenguoichoi;

    //TODO rename method
    public void setUpChuPhong(String waitingroomnamechuphong){
        this.waitingroomnamechuphong.setText(waitingroomnamechuphong);
        this.waitingroomnamenguoichoi.setText("Waiting");
    }
    public void setUpNguoiChoi(String chuphong,String waitingroomnamenguoichoi){
        this.waitingroomnamechuphong.setText(chuphong);
        this.waitingroomnamenguoichoi.setText(waitingroomnamenguoichoi);
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
            mainPageController.setup2();


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

