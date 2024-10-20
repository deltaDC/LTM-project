// GET INFORMATION USER (WIN , LOSS, DRAW, POINT)
package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AchievementController {
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
//    private UserSession usersessions;
//    private Session session; , Session session, UserSession usersessions
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
//        this.session = session;
//        this.usersessions = usersessions;
    }

    public void ClickHome(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }
}
