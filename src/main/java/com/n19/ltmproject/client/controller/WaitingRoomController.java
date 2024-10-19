package com.n19.ltmproject.client.controller;

// CLICK STARTGAME ( XAC NHAN DA READY CHUA)
import java.io.IOException;

import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.n19.ltmproject.client.handler.ServerHandler;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private UserSession usersessions;
    private Session session;
    public void setPrimaryStage(Stage stage, Session session, UserSession usersessions) {
        this.primaryStage = stage;
        this.session = session;
        this.usersessions = usersessions;
    }

    public void ClickExit(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage,session,usersessions);

        primaryStage.setScene(scene);
    }

    public void ClickStart(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent GamePlayViewParent = loader.load();
        Scene scene = new Scene(GamePlayViewParent);

        GamePlayController gameController = loader.getController();
        gameController.setPrimaryStage(primaryStage,session,usersessions);

        primaryStage.setScene(scene);
    }
}

