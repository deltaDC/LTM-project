package com.n19.ltmproject.client.controller;

// LAY DANH SACH NGUOI ON-LinE
//LAY DANH SACH NGUOI IN_GAME
// GUI LOI MOI
import java.io.IOException;
import java.net.URL;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.model.enums.PlayerStatus;
import com.n19.ltmproject.client.service.MessageService;

import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
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
import javafx.scene.control.Alert;
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
    private UserSession usersessions;
    private Session session;
    private final Gson gson = new Gson();
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private MessageService messageService = new MessageService(serverHandler);
    private Thread listenerThread;
    private boolean isInvitation = false;

    private boolean isListening = false;


    public void setPrimaryStage(Stage stage, Session session, UserSession usersessions) {
        this.primaryStage = stage;
        this.session = session;
        this.usersessions = usersessions;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new MessageService(serverHandler);
        loadPlayers();

//         Start the listener thread only once
        if (listenerThread == null || !listenerThread.isAlive()) {
            startListeningToServer();

        }
    }

    private void loadPlayers() {
        try {
            Map<String, Object> params = Map.of();
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

                    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            System.out.println("Selected items: " + newSelection.getUsername());
                        }
                    });
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void startListeningToServer() {
        isListening = true;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (isListening && isInvitation) {
                        String serverMessage = serverHandler.receiveMessage();

                        if (serverMessage != null) {
                            System.out.println("[MainPageController - startListeningToServer] Received from server: " + serverMessage);

                            // Phân tích JSON thành đối tượng Response
                            Response response = gson.fromJson(serverMessage, Response.class);

                            // Xử lý các hành động dựa trên response
                            if ("OK".equalsIgnoreCase(response.getStatus())) {
                                System.out.println("Bạn đã nhận được lời mời tham gia trò chơi!");
                                System.out.println(response.getMessage());
                                System.out.println(response.getData());

                                // Thực hiện cập nhật giao diện trên luồng JavaFX
                                Platform.runLater(MainPageController.this::moveToInvitationPage);
                            }
                        }

                        // Thêm khoảng nghỉ nhỏ để tránh việc đợi bận
                        Thread.sleep(100);
                    }
                } catch (Exception ex) {
                    System.out.println("Lỗi khi nhận tin nhắn từ server: " + ex.getMessage());
                }
                return null;
            }
        };

        // Khởi chạy task trong một luồng mới để giữ giao diện không bị đơ
        listenerThread = new Thread(task);
        listenerThread.setDaemon(true); // Đảm bảo luồng sẽ dừng khi ứng dụng đóng
        listenerThread.start();
    }


    private void moveToInvitationPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
            Parent invitationParent = loader.load();

            InvitationController inviteController = loader.getController();
            inviteController.setPrimaryStage(primaryStage,session,usersessions);

            Scene scene = new Scene(invitationParent);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ClickLogout(ActionEvent e) throws IOException {
        Player currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            System.out.println("Current logged-in user: " + currentUser);
        } else {
            System.out.println("No user is currently logged in.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", currentUser.getUsername());
        Response response = messageService.sendRequest("logout", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            // clear user
            SessionManager.clearSession();
            AlertController.showInformationAlert("Logout", "Logout successfully!");

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
            Parent loginViewParent = loader.load();
            Scene scene = new Scene(loginViewParent);
            stage.setScene(scene);
        }
        else {
            AlertController.showErrorAlert("Logout", "Something error...Please try again!");
        }
    }


    public void ClickAchievement(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Achievement.fxml"));

        Parent AchievementViewParent = loader.load();
        Scene scene = new Scene(AchievementViewParent);

        AchievementController achievementController = loader.getController();
        achievementController.setPrimaryStage( primaryStage,session,usersessions);

        primaryStage.setScene(scene);
    }

    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));

        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setPrimaryStage( primaryStage,session,usersessions);

        primaryStage.setScene(scene);
    }


    public void ClickInvitePlayer(ActionEvent event) throws IOException {

        Player selectedPlayer = table.getSelectionModel().getSelectedItem();

        if (selectedPlayer != null) {
            Map<String, Object> params = new HashMap<>();
            //TODO implement real token here to fetch the inviter
            params.put("inviter", "user1");
            params.put("invitee", selectedPlayer.getUsername());

            Response response = messageService.sendRequest("invitation", params);

            if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                System.out.println("Invitation sent");
                moveToWaitingRoom();
            } else {
                System.out.println("Invitation failed");
            }
        } else {
            System.out.println("Please choose a player to invite!");
        }
    }


    private void moveToWaitingRoom() throws IOException {
        Stage stage = (Stage) table.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
        Parent root = loader.load();

        WaitingRoomController waitingRoomController = loader.getController();
        waitingRoomController.setPrimaryStage(primaryStage, session, usersessions);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void ClickRefresh(ActionEvent actionEvent) {
        loadPlayers();

    }
}
