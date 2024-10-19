package com.n19.ltmproject.client.controller;

// LAY DANH SACH NGUOI ON-LinE
//LAY DANH SACH NGUOI IN_GAME
// GUI LOI MOI
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.model.enums.PlayerStatus;
import com.n19.ltmproject.client.service.MessageService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private MessageService messageService;
    private final Gson gson = new Gson();
    private final ServerHandler serverHandler = ServerHandler.getInstance();


    private boolean isListening = false;

    //DEPRECATED
    public void setServerConnection( Stage stage) {

        this.primaryStage = stage;
//        this.messageService = new MessageService(serverHandler);

//        if (!isListening) {
//            startListeningToServer();
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new MessageService(serverHandler);

        loadPlayers();

        if (!isListening) {
            startListeningToServer();
        }
    }

    private void loadPlayers() {
        new Thread(() -> {
            try {
                Map<String, Object> params = Map.of();
                Response response = messageService.sendRequest("getAllPlayer", params);

                if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                    List<Player> players = gson.fromJson(new Gson().toJson(response.getData()), new TypeToken<List<Player>>() {}.getType());

                    Platform.runLater(() -> {
                        playerList = FXCollections.observableArrayList(players);
                        numberColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));
                        username.setCellValueFactory(new PropertyValueFactory<>("username"));
                        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

                        table.setItems(playerList);
                        table.setFocusTraversable(false);
                        table.getSelectionModel().clearSelection();

                        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                            if (newSelection != null) {
                                System.out.println("Selected items: " + newSelection.getUsername());
                            }
                        });
                    });
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startListeningToServer() {
//        isListening = true;
//
//        new Thread(() -> {
//            try {
//                while (true) {
//                    String serverMessage = serverHandler.receiveMessage();
//                    if (serverMessage != null) {
//                        System.out.println("Received from server: " + serverMessage);
//                        if (serverMessage.contains("Invite You Game")) {
//                            System.out.println("You've received an invitation to play a game!");
//                            moveInvitation();
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                System.out.println("Error receiving message from server: " + ex.getMessage());
//            }
//        }).start();
    }

//    private void moveToWaitingRoom() {
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
//                Parent waitViewParent = loader.load();
//
//                WaitingRoomController waitingRoomController = loader.getController();
//                waitingRoomController.setServerConnection( primaryStage);
//
//                Scene scene = new Scene(waitViewParent);
//                primaryStage.setScene(scene);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
private void moveToWaitingRoom() throws IOException {
    Stage stage = (Stage) table.getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
    Parent root = loader.load();

    WaitingRoomController waitingRoomController = loader.getController();
    waitingRoomController.setServerConnection( primaryStage);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
}

    private void moveInvitation() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
                Parent invitationParent = loader.load();

                InvitationController inviteController = loader.getController();
                inviteController.setServerConnection( primaryStage);

                Scene scene = new Scene(invitationParent);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void ClickLogout(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
        Parent loginViewParent = loader.load();
        Scene scene = new Scene(loginViewParent);
        stage.setScene(scene);
    }

    public void ClickAchievement(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Achievement.fxml"));

        Parent AchievementViewParent = loader.load();
        Scene scene = new Scene(AchievementViewParent);

        AchievementController achievementController = loader.getController();
        achievementController.setServerConnection( primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));

        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setServerConnection( primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickInvitePlayer(ActionEvent event) throws IOException {
        Player selectedPlayer = table.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
//            serverHandler.sendMessage("Invite:" + selectedPlayer.getUsername());
            Map<String, Object> params = new HashMap<>();
            params.put("username", selectedPlayer.getUsername());

            Response response = messageService.sendRequest("Invitation", params);
            if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                System.out.println("Invitation sent");
                moveToWaitingRoom();
            } else {
                System.out.println("Invitation failed");
            }

//
        } else {
            System.out.println("Please choose a player to invite!");
        }


    }
}
