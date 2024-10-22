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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.Player;

public class MainPageController  {

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
    private volatile boolean running = true;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
//        loadPlayers();
    }
    public void setThread(){
        this.running =true;
        System.out.println("Thread in setThread");
        startListeningForInvite();

    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        messageService = new MessageService(serverHandler);
////        startListeningForInvite();;
//
//        loadPlayers();
//    }

    private void loadPlayers() {
        try {
            Map<String, Object> params = Map.of();
            this.running = false;
            Response response = messageService.sendRequest("getAllPlayer", params);

            Platform.runLater(() -> {
                System.out.println("Dang tao bang");
                if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                    List<Player> players = gson.fromJson(new Gson().toJson(response.getData()), new TypeToken<List<Player>>() {}.getType());
                    playerList = FXCollections.observableArrayList(players);
                    numberColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));
                    username.setCellValueFactory(new PropertyValueFactory<>("username"));
                    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

                    table.setItems(playerList);
                    System.out.println("TAO Bang thanh cong");
                    table.setFocusTraversable(false);
                    table.getSelectionModel().clearSelection();
                    this.running = true;
//                    setThread();
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            });Platform.runLater(() -> {
                System.out.println("Đang tạo bảng");
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
//        setThread();;

    }


    public void startListeningForInvite() {
        System.out.println("Start thread");
        new Thread(() -> {
            try {

                while (this.running ) {
                    System.out.println("running: "+this.running+" IN THREAD");
                    if(!this.running){
                        break;
                    }
                    String serverMessage = serverHandler.receiveMessage();
                    System.out.println("Received from server: " + serverMessage);
                    if (serverMessage != null && serverMessage.contains("Invite You Game")) {
                        Platform.runLater(() -> {
                            this.running = false;
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
                                Scene scene = new Scene(loader.load());
                                InvitationController tes=loader.getController();
                                tes.setPrimaryStage(primaryStage);

                                primaryStage.setScene(scene);
                                primaryStage.setTitle("Giao diện phòng chờ");
                                primaryStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                }
                System.out.println("END THREAD");
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }

    public void ClickLogout(ActionEvent e) throws IOException {
        this.running=false;
        serverHandler.sendMessage("NGATLISTENING");
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
//            Player currentUser = SessionManager.getCurrentUser();
//            Map<String, Object> params = new HashMap<>();
//            params.put("inviter", currentUser.getUsername());
//            params.put("invitee", selectedPlayer.getUsername());
//            this.running =false;
//            Response response = messageService.sendRequest("invitation", params);
//
//            if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
//                System.out.println(response.getMessage());
            moveToWaitingRoom();


            serverHandler.sendMessage("Invite:" + selectedPlayer.getUsername());

        } else {
            System.out.println("Invitation failed");
        }
//    }
////            System.out.println("Please choose a player to invite!");
//        }
    }

    private void moveToWaitingRoom() {
        this.running=false;
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
        this.running=false;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Achievement.fxml"));

        Parent AchievementViewParent = loader.load();
        Scene scene = new Scene(AchievementViewParent);

        AchievementController achievementController = loader.getController();
        achievementController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickLeaderBoard(ActionEvent e) throws IOException {
        this.running=false;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/LeaderBoard.fxml"));

        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);

        LeaderBoardController boardController = loader.getController();
        boardController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickRefresh(ActionEvent actionEvent) {
        this.running=false;
        serverHandler.sendMessage("NGATLISTENING");
        setup2();
    }
    @FXML
    public void setup() {
        messageService = new MessageService(serverHandler);
//        this.running = true;
//        startListeningForInvite();
//        System.out.println("thread trong setUp");
//        startListeningForInvite();
        System.out.println("Load bang trong setUp");
        loadPlayers();

//        namePlayer.setText(dataUser.getUsername());
    }
    @FXML
    public void setup2() {
        messageService = new MessageService(serverHandler);
        System.out.println("Load bang trong setUp");
        loadPlayers();
//        namePlayer.setText(dataUser.getUsername());
        setThread();
    }

}
