package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.model.enums.PlayerStatus;
import com.n19.ltmproject.client.service.MessageService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.Player;
import javafx.util.Duration;

public class MainPageController  {

    @FXML
    private TableView<Player> table;

    @FXML
    private TableColumn<Player, String> username;

    @FXML
    private TableColumn<Player, Integer> numberColumn;

    @FXML
    private TableColumn<Player, PlayerStatus> statusColumn;

    @FXML
    private Label mainPageUsername;

    private ObservableList<Player> playerList;
    private Stage primaryStage;
    private final Gson gson = new Gson();
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private MessageService messageService;

    //TODO rename for better readability
    private volatile boolean running = true;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void setup() {
        mainPageUsername.setText(SessionManager.getCurrentUser().getUsername());
        messageService = new MessageService(serverHandler);
        System.out.println("Load bang trong setUp");
        loadPlayers();
    }

    @FXML
    public void setupMainPage() {
        mainPageUsername.setText(SessionManager.getCurrentUser().getUsername());
        messageService = new MessageService(serverHandler);
        System.out.println("Load bang trong setUp");
        loadPlayers();
        setThread();
    }

    public void setThread(){
        this.running = true;
        System.out.println("Thread in setThread");
        startListeningForInvite();
    }

    private void loadPlayers() {
        try {
            Map<String, Object> params = Map.of();
            this.running = false;
            Response response = messageService.sendRequest("getAllPlayer", params);

            Platform.runLater(() -> {
                if (response != null) {
                    System.out.println("Status từ server: " + response.getStatus());
                    System.out.println("Dữ liệu nhận được: " + response.getData());
                }

                if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                    List<Player> players = gson.fromJson(new Gson().toJson(response.getData()), new TypeToken<List<Player>>() {}.getType());
                    playerList = FXCollections.observableArrayList(players);
                    numberColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));
                    username.setCellValueFactory(new PropertyValueFactory<>("username"));
                    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

                    table.setItems(playerList);
                    System.out.println("Tạo bảng thành công");
                    table.setFocusTraversable(false);
                    table.getSelectionModel().clearSelection();
                    this.running = true;
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListeningForInvite() {
        System.out.println("Start thread");
        new Thread(this::listenForServerMessages).start();
    }

    private void listenForServerMessages() {
        try {
            while (this.running) {
                if (!this.running) {
                    break;
                }
                String serverMessage = serverHandler.receiveMessage();
                System.out.println("Received from server: " + serverMessage);

                if (serverMessage != null && serverMessage.contains("INVITATION GAME")) {
                    Platform.runLater(() -> handleInvitation(serverMessage));
                }
            }
            System.out.println("END THREAD");
        } catch (IOException ex) {
            System.out.println("Error receiving message from server: " + ex.getMessage());
        }
    }

    private void handleInvitation(String serverMessage) {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
            Scene invitationScene = new Scene(loader.load());

            InvitationController invitationController = loader.getController();
            invitationController.setPrimaryStage(primaryStage);

            String userInvite = serverMessage.split(" ")[0];
            invitationController.setUpInvitation(userInvite, userInvite);

            Timeline timeline = createReturnToMainPageTimeline();
            invitationController.setTimeline(timeline);

            primaryStage.setScene(invitationScene);
            primaryStage.setTitle("Giao diện phòng chờ");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Timeline createReturnToMainPageTimeline() {
        return new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
                Parent mainPageViewParent = mainLoader.load();
                Scene mainScene = new Scene(mainPageViewParent);

                MainPageController mainPageController = mainLoader.getController();
                mainPageController.setPrimaryStage(primaryStage);

                primaryStage.setScene(mainScene);
                serverHandler.sendMessage("STOP_LISTENING");
                mainPageController.setupMainPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public void ClickLogout(ActionEvent e) throws IOException {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");
        Player currentUser = SessionManager.getCurrentUser();
        Map<String, Object> params = new HashMap<>();
        params.put("username", currentUser.getUsername());
        this.running = false;
        Response response = messageService.sendRequest("logout", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
            Parent loginViewParent = loader.load();
            Scene scene = new Scene(loginViewParent);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            this.running = true;
        } else {
            System.out.println("Logout failed");
        }
    }


    public void ClickInvitePlayer(ActionEvent event) throws IOException {

        Player selectedPlayer = table.getSelectionModel().getSelectedItem();

        if (selectedPlayer != null) {

            this.running = false;
            moveToWaitingRoom();

            serverHandler.sendMessage(SessionManager.getCurrentUser().getUsername()+" Invite:" + selectedPlayer.getUsername());

            serverHandler.sendMessage("STOP_LISTENING");
        } else {
            System.out.println("Invitation failed");
        }
    }

    private void moveToWaitingRoom() {
        this.running = false;

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
                Parent waitViewParent = loader.load();

                WaitingRoomController waitingRoomController = loader.getController();
                waitingRoomController.setPrimaryStage(primaryStage);
                waitingRoomController.setUpHost(SessionManager.getCurrentUser().getUsername());

                Scene scene = new Scene(waitViewParent);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void ClickAchievement(ActionEvent e) throws IOException {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Achievement.fxml"));

        Parent AchievementViewParent = loader.load();
        Scene scene = new Scene(AchievementViewParent);

        AchievementController achievementController = loader.getController();
        achievementController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));
        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickRefresh(ActionEvent actionEvent) {
        this.running = false;
        serverHandler.sendMessage("STOP_LISTENING");
        setupMainPage();
    }
}
