package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
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
import lombok.Setter;

public class InvitationController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    @Setter
    private Stage primaryStage;

    @Setter
    private Timeline timeline;

    @FXML
    private Button inviter;

    @FXML
    private Label invitationProfile;

    private String inviterName;
    private long inviterId;
    private long inviteeId;

    public void setUpInvitation(String inviterName, long inviterId, long inviteeId, String invitationProfile) {
        this.inviterName = inviterName;
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.inviter.setText(inviterName.toUpperCase() + " INVITE YOU");
        this.invitationProfile.setText(invitationProfile);
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }

        sendAcceptanceToServer(this.inviterName, SessionManager.getCurrentUser().getUsername(), inviterId, inviteeId);
        loadWaitingRoom();
    }

    private void sendAcceptanceToServer(String inviterPlayer, String currentAccepterPlayer, long inviterId, long inviteeId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", currentAccepterPlayer);
        params.put("inviterName", inviterPlayer);
        params.put("inviterId", inviterId);
        params.put("inviteeId", inviteeId);

        String message = createMessage("userJoinedRoom", params);
        serverHandler.sendMessage(message);
    }

    private void loadWaitingRoom() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController waitingRoomController = loader.getController();
        waitingRoomController.setPrimaryStage(primaryStage);
        waitingRoomController.setUpPlayer(this.inviterName, SessionManager.getCurrentUser().getUsername(), this.inviterId, SessionManager.getCurrentUser().getId());

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("inviter", SessionManager.getCurrentUser().getUsername());
        params.put("invitee", inviterName);
        params.put("inviteeId", inviterId);
        params.put("inviterId", inviteeId);

        Response response = messageService.sendRequest("refuseInvitation", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            moveToMainPage();
        } else {
            System.out.println("REFUSED failed: " + (response != null ? response.getMessage() : "Unknown error"));
        }
    }

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

    private String createMessage(String action, HashMap<String, Object> params) {
        return "{\"action\":\"" + action + "\", \"params\":" + new Gson().toJson(params) + "}";
    }
}