package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.model.enums.PlayerStatus;
import com.n19.ltmproject.client.service.MessageService;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.Player;

public class MainPageController implements Initializable {

    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> username;
    @FXML
    private TableColumn<Player, Integer> numberColumn;
    @FXML
    private TableColumn<Player, PlayerStatus> statusColumn;

    private ObservableList<Player> playerList;
    private Stage primaryStage;
    private final Gson gson = new Gson();
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private MessageService messageService;
    public static boolean isListening = true;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        loadPlayers();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new MessageService(serverHandler);
        startListeningToServer();
        loadPlayers();
    }

    private void loadPlayers() {
        try {
            Map<String, Object> params = Map.of();
            isListening = false;
            Response response = messageService.sendRequest("getAllPlayer", params);

            Platform.runLater(() -> {
                if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                    List<Player> players = gson.fromJson(new Gson().toJson(response.getData()), new TypeToken<List<Player>>() {}.getType());
                    playerList = FXCollections.observableArrayList(players);
                    numberColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));
                    username.setCellValueFactory(new PropertyValueFactory<>("username"));
                    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

                    table.setItems(playerList);
                    table.setFocusTraversable(false);
                    table.getSelectionModel().clearSelection();
                    isListening = true;
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListeningToServer() {
        new Thread(() -> {
            try {
                while (isListening) {
                    String serverMessage = serverHandler.receiveMessage();
                    if (serverMessage != null) {
                        System.out.println("Received from server: " + serverMessage);
                        if (serverMessage.contains("Invite You Game")) {
                            Platform.runLater(this::moveInvitation);
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }


    private void moveInvitation() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
            Parent invitationParent = loader.load();

            InvitationController inviteController = loader.getController();
            inviteController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(invitationParent);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ClickLogout(ActionEvent e) throws IOException {
        Player currentUser = SessionManager.getCurrentUser();
        Map<String, Object> params = new HashMap<>();
        params.put("username", currentUser.getUsername());
        Response response = messageService.sendRequest("logout", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
            Parent loginViewParent = loader.load();
            Scene scene = new Scene(loginViewParent);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } else {
            System.out.println("Logout failed");
        }
    }



    public void ClickInvitePlayer(ActionEvent event) throws IOException {
        Player selectedPlayer = table.getSelectionModel().getSelectedItem();

        if (selectedPlayer != null){
            Player currentUser = SessionManager.getCurrentUser();
            Map<String, Object> params = new HashMap<>();
            params.put("inviter", currentUser.getUsername());
            params.put("invitee", selectedPlayer.getUsername());
            isListening = false;
            Response response = messageService.sendRequest("invitation", params);
            isListening = true;
            if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                System.out.println(response.getMessage());
                moveToWaitingRoom();
                serverHandler.sendInvite(selectedPlayer.getUsername());
            } else {
                System.out.println("Invitation failed");
            }
        } else {
            System.out.println("Please choose a player to invite!");
        }
    }

    private void moveToWaitingRoom() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
                Parent waitViewParent = loader.load();

                WaitingRoomController waitingRoomController = loader.getController();
                waitingRoomController.setPrimaryStage(primaryStage);

                Scene scene = new Scene(waitViewParent);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void ClickAchievement(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Achievement.fxml"));

        Parent AchievementViewParent = loader.load();
        Scene scene = new Scene(AchievementViewParent);

        AchievementController achievementController = loader.getController();
        achievementController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));

        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickRefresh(ActionEvent actionEvent) {
        loadPlayers();
    }
}
