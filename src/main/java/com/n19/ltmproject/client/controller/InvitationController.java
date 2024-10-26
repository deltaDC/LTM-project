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

    public void setUpInvitation(String inviterName, String invitationProfile) {
        this.inviterName = inviterName;
        this.inviter.setText(inviterName.toUpperCase() + " INVITE YOU");
        this.invitationProfile.setText(invitationProfile);
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }

        sendAcceptanceToServer(this.inviterName, SessionManager.getCurrentUser().getUsername());
        loadWaitingRoom();
    }

    private void sendAcceptanceToServer(String inviterPlayer, String currentAccepterPlayer) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", currentAccepterPlayer);
        params.put("inviterName", inviterPlayer);
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
        waitingRoomController.setUpPlayer(this.inviterName, SessionManager.getCurrentUser().getUsername());

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop();
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("invitee", SessionManager.getCurrentUser().getUsername());
        params.put("inviter", inviterName);
//        String refuseMessage = createMessage("refuseInvitation", params);
        Response response = messageService.sendRequest("refuseInvitation", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
//            this.running = false;
//            serverHandler.sendMessage("STOP_LISTENING");
//            moveToWaitingRoom(selectedPlayer);
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
