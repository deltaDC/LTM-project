package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.n19.ltmproject.client.model.dto.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.n19.ltmproject.client.handler.ServerHandler;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private volatile boolean running = true;
    private Thread listenerThread;
    private volatile boolean isCountdownRunning = true;

    @FXML
    private Label waitingRoomHostName;
    @FXML
    private Label waitingRoomPlayerName;
    @FXML
    private Button inviterButton;
    @FXML
    private Button inviteeButton;
    @FXML
    private Label countdownLabel;

    private int countdownTime = 10;

    public void setUpHost(String waitingRoomHostName, String waitingRoomPlayerName) {
        this.waitingRoomHostName.setText(waitingRoomHostName);
        this.waitingRoomPlayerName.setText(waitingRoomPlayerName);
    }

    public void setUpPlayer(String host, String waitingRoomPlayerName) {
        this.waitingRoomHostName.setText(host);
        this.waitingRoomPlayerName.setText(waitingRoomPlayerName);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        inviterButton.setText("READY");
        inviteeButton.setText("WAITING");
        startListeningForInvitee();
    }

    public void startListeningForInvitee() {
        System.out.println("Listening for player 2 to join room...");

        listenerThread = new Thread(() -> {
            try {
                System.out.println(running);
                while (running) {
                    String serverMessage = serverHandler.receiveMessage();
                    System.out.println("Received from server: " + serverMessage);

                    if (serverMessage.startsWith("{")) {
                        try {
                            Response response = new Gson().fromJson(serverMessage, Response.class);

                            if ("SUCCESS".equals(response.getStatus())) {
                                updateUIWithJoinMessage(response.getMessage());

                            } else if ("REFUSED".equals(response.getStatus())) {
                                Platform.runLater(() -> {
                                    System.out.println("Player declined the invitation.");
                                    try {
                                        ClickExit();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        } catch (JsonSyntaxException e) {
                            System.out.println("Invalid JSON format: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        });
        listenerThread.start();
    }

    private void updateUIWithJoinMessage(String message) {
        String regex = "\\[JOINED\\] User (\\w+) đã tham gia phòng với (\\w+)\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String inviteeUsername = matcher.group(1);
//            String inviterUsername = matcher.group(2);

            Platform.runLater(() -> {
                inviteeButton.setText("READY");
                waitingRoomPlayerName.setText(inviteeUsername);
                startCountdown();
                stopListening();
            });
        } else {
            System.out.println("Could not extract usernames from message.");
        }
    }

    private void stopListening() {
        running = false;
        serverHandler.sendMessage("STOP_LISTENING");
//        if (listenerThread != null && listenerThread.isAlive()) {
//            listenerThread.interrupt();
//        }
    }

    private void startCountdown() {
        countdownLabel.setText(String.valueOf(countdownTime));
        isCountdownRunning = true; // Bắt đầu đếm ngược
        new Thread(() -> {
            while (countdownTime > 0 && isCountdownRunning) {
                try {
                    Thread.sleep(1000);
                    countdownTime--;
                    Platform.runLater(() -> countdownLabel.setText(String.valueOf(countdownTime)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            // Chỉ khi đếm ngược kết thúc một cách tự nhiên mới bắt đầu trò chơi
            if (isCountdownRunning) {
                Platform.runLater(this::startGame);
            }
        }).start();
    }

    private void startGame() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ClickExit() throws IOException {
        // Dừng đếm ngược khi người dùng nhấn "Exit"
        isCountdownRunning = false;
        stopListening();
        // Điều hướng về MainPage
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        mainPageController.setupMainPage();
    }

    @FXML
    void ClickStart(ActionEvent event) {
        if ("READY".equals(inviteeButton.getText())) {
            startGame();
        } else {
            System.out.println("Both players need to be ready.");
        }
    }
}