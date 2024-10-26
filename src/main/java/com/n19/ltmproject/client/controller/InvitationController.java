package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION


import java.io.IOException;

import com.n19.ltmproject.client.model.auth.SessionManager;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InvitationController {

    private Stage primaryStage;
    private Timeline timeline;

    @FXML
    private Button invitationButton;
    @FXML
    private Label invitationProfile;

    private String newPlayer;

    public void setUpInvitation(String invitationButton, String invitationProfile){
        this.newPlayer = invitationButton;
        this.invitationButton.setText(invitationButton.toUpperCase() + " INVITE YOU");
        this.invitationProfile.setText(invitationProfile);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setPrimaryStage(primaryStage);
        WaitingRoomParentController.setUpPlayer(this.newPlayer, SessionManager.getCurrentUser().getUsername());

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }
        moveToMainPage();
    }

    //TODO refactor method name
    public void ClickInviteX(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }
        moveToMainPage();
    }

    private void moveToMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);

        mainPageController.setupMainPage();
    }
}