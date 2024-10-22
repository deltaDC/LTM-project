package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InvitationController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;


    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }





    public void ClickAccept(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {

        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }
    public void ClickInviteX(ActionEvent e) throws IOException {
        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }

    private void moveToMainPage() throws IOException {
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
    }
}