package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION


import java.io.IOException;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.service.MessageService;
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

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);
    private Stage primaryStage;
    private Timeline timeline;

    @FXML
    private Button inviter;
    @FXML
    private Label invitationProfile;

    private String inviterName;

    public void setUpInvitation(String inviterName, String invitationProfile){
        this.inviterName = inviterName;
        this.inviter.setText(inviterName.toUpperCase() + " INVITE YOU");
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

        sendAcceptanceToServer(this.inviterName, SessionManager.getCurrentUser().getUsername());

        loadWaitingRoom();
    }

    private void sendAcceptanceToServer(String inviterPlayer, String currentAccepterPlayer) {

        serverHandler.sendMessage("ACCEPT_INVITATION " + inviterPlayer + " " + currentAccepterPlayer);
    }

    private void loadWaitingRoom() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController waitingRoomController = loader.getController();
        waitingRoomController.setPrimaryStage(primaryStage);
        waitingRoomController.setUpPlayer(this.inviterName, SessionManager.getCurrentUser().getUsername());

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }
        moveToMainPage();
    }

    //TODO refactor method name
    public void CloseInvitation(ActionEvent e) throws IOException {
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