package com.n19.ltmproject.client.controller;

// CLICK STARTGAME ( XAC NHAN DA READY CHUA)
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.n19.ltmproject.client.handler.ServerHandler;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;

    public void setServerConnection( Stage stage) {
        this.primaryStage = stage;
    }

    public void ClickExit(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setServerConnection(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickStart(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent GamePlayViewParent = loader.load();
        Scene scene = new Scene(GamePlayViewParent);

        GamePlayController gameController = loader.getController();
        gameController.setServerConnection(primaryStage);

        primaryStage.setScene(scene);
    }
}

