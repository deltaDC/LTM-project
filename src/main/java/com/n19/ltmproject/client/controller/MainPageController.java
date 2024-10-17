package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.enums.PlayerStatus;
import com.n19.ltmproject.client.service.SendInvitationService;
import com.n19.ltmproject.client.service.SendResultService;
import javafx.application.Platform;
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

//    @FXML
//    private TableView<Player> table;
//
//    @FXML
//    private TableColumn<Player, String> nameColumn;
//
//    @FXML
//    private TableColumn<Player, Integer> pointColumn;
//
//    @FXML
//    private TableColumn<Player, PlayerStatus> statusColumn;
//
//    private ObservableList<Player> playerList;
    private ServerHandler serverHandler;
    private Stage primaryStage;

    private boolean isListening = false;


    public void setServerConnection(ServerHandler serverHandler, Stage stage) {
        this.serverHandler = serverHandler;
        this.primaryStage = stage;

        if (!isListening) {
            startListeningToServer();
        }
    }

    public void startListeningToServer() {
        isListening = true;

        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = serverHandler.receiveMessage();
                    if (serverMessage != null) {
                        System.out.println("Received from server: " + serverMessage);
                        if (serverMessage.contains("Invite You Game")) {
                            System.out.println("You've received an invitation to play a game!");
                            moveInvitation();
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }

    private void moveToWaitingRoom() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
                Parent waitViewParent = loader.load();

                WaitingRoomController waitingRoomController = loader.getController();
                waitingRoomController.setServerConnection(serverHandler,primaryStage);

                Scene scene = new Scene(waitViewParent);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void moveInvitation() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
                Parent invitationParent = loader.load();

                InvitationController inviteController = loader.getController();
                inviteController.setServerConnection(serverHandler,primaryStage);

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
        achievementController.setServerConnection(serverHandler,primaryStage);

        primaryStage.setScene(scene);
    }
    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));

        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setServerConnection(serverHandler, primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickInvitePlayer(ActionEvent event) {
//        Player selectedPlayer = table.getSelectionModel().getSelectedItem();
//        if (selectedPlayer != null) {
//            serverHandler.sendMessage("Invite:" + selectedPlayer.getUsername());
//
//            moveToWaitingRoom();
//        } else {
//            System.out.println("Please choose a player to invite!");
//        }
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.connect("127.0.0.1", 1234);

        // Tạo SendResultService
        SendInvitationService sendinvite = new SendInvitationService(serverHandler);

        // Gửi kết quả trận đấu
        String userId = "user2";  // ID của đối thủ

        try {
            // Gửi kết quả và nhận phản hồi
            String responseMessage = sendinvite.sendInvitation(userId);
            // In ra phản hồi
            System.out.println("Server response: " + responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //   Ngắt kết nối
            try {
                serverHandler.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        playerList = FXCollections.observableArrayList(
////            new Player("user1", "1", "chinh@gmail.com"),
////            new Player("user2", "1", "chinh@gmail.com"),
////            new Player("user3", "1", "chinh@gmail.com"),
////            new Player("user4", "1", "chinh@gmail.com"),
////            new Player("user5", "1", "chinh@gmail.com"),
////            new Player("user6", "1", "chinh@gmail.com")
//
//        );
//
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
//        pointColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
//        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//
//        table.setItems(playerList);
//        table.setFocusTraversable(false);
//        table.getSelectionModel().clearSelection();
//
//        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                System.out.println("Selected items: " + newSelection.getUsername());
//            }
//        });
    }
}
