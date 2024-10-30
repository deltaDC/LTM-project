package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);
    private volatile boolean running = true;
    private volatile boolean isOpponentExit = false;

    @FXML
    private Label resultLabel;

    @FXML
    private Label opponentExitLabel;

    @FXML
    private Label scoreLabel;
    @FXML
    private Button playAgainButton;
    @FXML
    private Label playAgainPane;
    @FXML
    private Label playAgainMessage;
    @FXML
    private Button playAgainAcceptButton;
    @FXML
    private Button playAgainRefuseButton;

    private boolean isWinner;
    private boolean isDraw;
    private String opponent;
    private long gameId;
    private Stage primaryStage;
    @FXML
    private Label currentPlayerNameLabel;
    @FXML
    private Label opponentPlayerNameLabel;

    private long currentPlayerId;
    private long opponentPlayerId;
    private String username;
    private  String opponentName;

    public void setUpPlayerID(long playerId, long opponentId,String username, String opponentName) {
        this.currentPlayerId = playerId;
        this.opponentPlayerId = opponentId;
        this.username = username;
        this.opponentName = opponentName;
    }

    public void setPlayerNames(String currentPlayerName, String opponentPlayerName) {
        currentPlayerNameLabel.setText(currentPlayerName);
        opponentPlayerNameLabel.setText(opponentPlayerName);
    }

    public void setResults(String results, String score, boolean isWinner, boolean isDraw) {
        this.isWinner = isWinner;
        this.isDraw = isDraw;
        scoreLabel.setText(score);
        resultLabel.setText(isDraw ? "Trận đấu hòa!" : (isWinner ? "Bạn đã thắng!" : "Bạn đã thua!"));
    }

    @FXML
    private void handleExit() {
        stopListening();
        if(!isOpponentExit){
            HashMap<String, Object> params = new HashMap<>();
            params.put("username",username);
            params.put("opponent", opponentName);
            params.put("userId",currentPlayerId);
            params.put("opponentId", opponentPlayerId);

            Response response =messageService.sendRequestAndReceiveResponse("exitResult", params);
            if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                loadMainPage();
            } else {
                System.out.println("Invitation failed: " + (response != null ? response.getMessage() : "Unknown error"));
            }
        }
        else{
            loadMainPage();
        }
    }

    @FXML
    private void handlePlayAgain() {
        stopListening();
        sendResultToServer();
        HashMap<String, Object> params = new HashMap<>();
        params.put("username",username);
        params.put("opponent", opponentName);
        params.put("userId",currentPlayerId);
        params.put("opponentId", opponentPlayerId);

        Response response = messageService.sendRequestAndReceiveResponse("playagain", params);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            moveToWaitingRoom();
        } else {
            System.out.println("Invitation failed: " + (response != null ? response.getMessage() : "Unknown error"));
        }

    }
    public void startListeningForInvitee() {
        System.out.println("Listening for player1 quit game ...");

       new Thread(() -> {
            try {
                while (running) {
                    if (!this.running) {
                        break;
                    }
                    System.out.println("Thread in Result");
                    String serverMessage = serverHandler.receiveMessage();
                    System.out.println("THREAD: " + serverMessage);
                    if (serverMessage.contains("EXITRESULT")) {
                        isOpponentExit = true;
                        playAgainButton.setVisible(false);
                        opponentExitLabel.setVisible(true);
                    }
                    if (serverMessage.contains("PlayAgain")) {
                        playAgainPane.setVisible(true);
                        playAgainMessage.setVisible(true);
                        playAgainAcceptButton.setVisible(true);
                        playAgainRefuseButton.setVisible(true);
                        System.out.println("Bạn nhận dc lời mời chs lại");
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();

    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        startListeningForInvitee();
    }

    private void sendResultToServer() {
        boolean isWinner = this.isWinner;
        boolean isDraw = this.isDraw;

        try {
            System.out.println("Dữ liệu gửi về server: " + String.format("EXIT_GAME {\"gameId\": %d, \"isWinner\": %b, \"isDraw\": %b}", gameId, isWinner, isDraw));

            messageService.sendRequestAndReceiveResponse("EXIT_GAME", Map.of(
                    "gameId", gameId,
                    "isWinner", isWinner,
                    "isDraw", isDraw
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không thể gửi kết quả về server");
        }
    }

    private void sendExitNotification() {
        Map<String, Object> params = Map.of("exitResult", opponent);
        Response response = messageService.sendRequestAndReceiveResponse("exitResult", params);
        System.out.println(
                response != null && "OK".equalsIgnoreCase(response.getStatus())
                ? "Kết quả thoát đã được xác nhận"
                : "Xác nhận kết quả thoát thất bại"
        );
    }

    private void loadMainPage() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            mainPageController.setupMainPage();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to load MainPage.fxml");
        }
    }

    private void stopListening() {
        running = false;
        serverHandler.sendMessage("STOP_LISTENING");
    }

    private void sendAcceptanceToServer(String inviterPlayer, String currentAccepterPlayer, long inviterId, long inviteeId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", currentAccepterPlayer);
        params.put("inviterName", inviterPlayer);
        params.put("inviterId", inviterId);
        params.put("inviteeId", inviteeId);

        messageService.sendRequestNoResponse("userJoinedRoom", params);
    }

    public void ClickAcceptPlayAgain(ActionEvent e) throws IOException {
        stopListening();
        sendAcceptanceToServer(opponentName, username, opponentPlayerId, currentPlayerId);
        loadWaitingRoom();
    }

    public void ClickRefusePlayAgain(ActionEvent e) throws IOException {
        stopListening();
        HashMap<String, Object> params = new HashMap<>();

        params.put("invitee", username);
        params.put("inviter", opponentName);
        params.put("inviterId", opponentPlayerId);
        params.put("inviteeId", currentPlayerId);

        Response response = messageService.sendRequestAndReceiveResponse("refuseInvitation", params);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            loadMainPage();

        } else {
            System.out.println("REFUSED failed: " + (response != null ? response.getMessage() : "Unknown error"));
        }
    }

    // đây là sang trang waitinh của người thách đấu lại
    private void moveToWaitingRoom() {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
                Parent waitViewParent = loader.load();

                WaitingRoomController waitingRoomController = loader.getController();
                waitingRoomController.setPrimaryStage(primaryStage);
                waitingRoomController.setUpHost(
                        currentPlayerId,
                        opponentPlayerId,
                        username,
                        opponentName
                );

                Scene scene = new Scene(waitViewParent);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // đây là sang trang waitng của người dc mời
    private void loadWaitingRoom() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController waitingRoomController = loader.getController();
        waitingRoomController.setPrimaryStage(primaryStage);
        waitingRoomController.setUpOpponent(
                opponentPlayerId,
                currentPlayerId,
                opponentName,
                username
        );

        primaryStage.setScene(scene);
    }

}
