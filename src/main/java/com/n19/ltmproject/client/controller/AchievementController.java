// GET INFORMATION USER (WIN , LOSS, DRAW, POINT)
package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AchievementController {
    private ServerHandler serverHandler;
    private Stage primaryStage;

    public void setServerConnection(ServerHandler serverHandler, Stage stage) {
        this.serverHandler = serverHandler;
        this.primaryStage = stage;
    }

    public void ClickHome(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setServerConnection(serverHandler,primaryStage);

        primaryStage.setScene(scene);
    }
}
