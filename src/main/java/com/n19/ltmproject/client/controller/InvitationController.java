package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.HashMap;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;
import javafx.animation.Timeline;
import javafx.application.Platform;
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

    @FXML
    private Button inviter;

    @FXML
    private Label invitationProfile;

    @FXML
    private Label countdownInvitation;

    private String inviterName;
    private long inviterId;
    private long inviteeId;
    private int invitationCountdownSeconds = 10;
    private volatile boolean isCountdownRunning = true;

    public void setUpInvitation(String inviterName, long inviterId, long inviteeId, String invitationProfile) {
        this.inviterName = inviterName;
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.inviter.setText(inviterName.toUpperCase() + " INVITE YOU");
        this.invitationProfile.setText(invitationProfile);
        createReturnToMainPageTimeline(inviterName, inviterId, inviteeId);
    }

    private void createReturnToMainPageTimeline(String userInvite, long inviterId, long inviteeId) {
        countdownInvitation.setText(String.valueOf(invitationCountdownSeconds));
        startCountdown(userInvite, inviterId, inviteeId);
    }

    private void startCountdown(String userInvite, long inviterId, long inviteeId) {
        new Thread(() -> {
            while (invitationCountdownSeconds > 0 && isCountdownRunning) {
                try {
                    Thread.sleep(1000);
                    invitationCountdownSeconds--;
                    Platform.runLater(() -> countdownInvitation.setText(String.valueOf(invitationCountdownSeconds)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if (isCountdownRunning) {
                sendRefusalAndMoveToMainPage(userInvite, inviterId, inviteeId);
            }
        }).start();
    }

    private void sendRefusalAndMoveToMainPage(String userInvite, long inviterId, long inviteeId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("invitee", SessionManager.getCurrentUser().getUsername());
        params.put("inviter", userInvite);
        params.put("inviterId", inviterId);
        params.put("inviteeId", inviteeId);

        Response response = messageService.sendRequestAndReceiveResponse("refuseInvitation", params);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            Platform.runLater(() -> {
                try {
                    moveToMainPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            System.out.println("REFUSED failed: " + (response != null ? response.getMessage() : "Unknown error"));
        }
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        isCountdownRunning = false;

        sendAcceptanceToServer(this.inviterName, SessionManager.getCurrentUser().getUsername(), inviterId, inviteeId);
        loadWaitingRoom();
    }

    private void sendAcceptanceToServer(String inviterPlayer, String currentAccepterPlayer, long inviterId, long inviteeId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", currentAccepterPlayer);
        params.put("inviterName", inviterPlayer);
        params.put("inviterId", inviterId);
        params.put("inviteeId", inviteeId);

        messageService.sendRequestNoResponse("userJoinedRoom", params);
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
        isCountdownRunning = false;

        HashMap<String, Object> params = new HashMap<>();

        params.put("invitee", SessionManager.getCurrentUser().getUsername());
        params.put("inviter", inviterName);
        params.put("inviterId", inviterId);
        params.put("inviteeId", inviteeId);

        Response response = messageService.sendRequestAndReceiveResponse("refuseInvitation", params);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            moveToMainPage();

        } else {
            System.out.println("REFUSED failed: " + (response != null ? response.getMessage() : "Unknown error"));
        }
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