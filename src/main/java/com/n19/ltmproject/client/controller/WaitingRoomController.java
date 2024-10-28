//Known bug:
//Khi trong thoi gian dem nguoc ma click ve trang main thi user con lai van chua back ve
//
//Click invitee click refuse invitation thi back lai main chua load lai dc bang
//
//Tao idGame moi khi countdown kethuc (done)



package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import com.n19.ltmproject.client.service.MessageService;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    private Stage primaryStage;
    private volatile boolean running = true;
    private Thread listenerThread;
    private volatile boolean isCountdownRunning = true;
    private boolean isInviter; // true nếu là chủ phòng, false nếu là người được mời

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
    @FXML
    private Label countdownText;

    private int countdownTime = 3;
    private long inviterId;
    private long inviteeId;
    private long gameId;

    public void setUpHost(String waitingRoomHostName, long inviterId, long inviteeId, String waitingRoomPlayerName) {
        this.waitingRoomHostName.setText(waitingRoomHostName);
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.waitingRoomPlayerName.setText(waitingRoomPlayerName);
        this.isInviter = true;
    }

    public void setUpPlayer(String host, String waitingRoomPlayerName, long inviterId, long inviteeId) {
        this.waitingRoomHostName.setText(host);
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.waitingRoomPlayerName.setText(waitingRoomPlayerName);
        this.isInviter = false;
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
                while (running) {
                    String serverMessage = serverHandler.receiveMessage();

                    if (serverMessage.contains("New game started! Game ID:")) {
                        String regex = "New game started! Game ID: (\\d+)";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(serverMessage);

                        if (matcher.find()) {
                            gameId = Long.parseLong(matcher.group(1));
                            System.out.println("Đã gán game ID: " + gameId);

                            running = false;
                            serverHandler.sendMessage("STOP_LISTENING");
                            startGame(gameId);
                        }
                    }
                    else if (serverMessage.startsWith("{")) {
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
        String regex = "\\[JOINED\\] User (\\w+) với playerId (\\d+) đã tham gia phòng với (\\w+) playerId (\\d+)\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String inviteeUsername = matcher.group(1);
            long inviteePlayerId = Long.parseLong(matcher.group(2));

            this.inviterId = Long.parseLong(matcher.group(4));
            this.inviteeId = inviteePlayerId;

            Platform.runLater(() -> {
                inviteeButton.setText("READY");
                inviteeButton.setStyle("-fx-background-color: #2BC9FC;");
                waitingRoomPlayerName.setText(inviteeUsername);
                countdownLabel.setVisible(true);
                countdownText.setVisible(true);
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
    }

    private void startCountdown() {
        countdownLabel.setText(String.valueOf(countdownTime));
        isCountdownRunning = true;
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

            if (isCountdownRunning) {
                if (isInviter) {
                    sendStartGameCommand();
                } else {
                    Platform.runLater(() -> startGame(gameId));
                }
            }
        }).start();
    }


    private void startGame(long gameId) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));
            Parent root = loader.load();

            GamePlayController gamePlayController = loader.getController();
            gamePlayController.setStage(primaryStage);
            gamePlayController.setGameId(gameId);

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Game Interface");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStartGameCommand() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("player1Id", inviterId);
        params.put("player2Id", inviteeId);

        Response response = messageService.sendRequestAndReceiveResponse("startNewGame", params);

        if ("OK".equals(response.getStatus())) {
            gameId = ((Number) ((Map<String, Object>) response.getData()).get("gameId")).longValue();
            System.out.println("New game started with gameId: " + gameId);

            Platform.runLater(() -> startGame(gameId));
        } else {
            System.out.println("Failed to start game: " + response.getMessage());
        }
    }


    @Deprecated
    private String createMessage(String action, HashMap<String, Object> params) {
        return "{\"action\":\"" + action + "\", \"params\":" + new Gson().toJson(params) + "}";
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
            startGame(gameId);
        } else {
            System.out.println("Both players need to be ready.");
        }
    }
}
