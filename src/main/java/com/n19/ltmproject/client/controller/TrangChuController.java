package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.n19.ltmproject.client.model.ServerConnection;
import com.n19.ltmproject.server.model.enums.PlayerStatus;
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
import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.enums.PlayerStatus;
public class TrangChuController implements Initializable {

    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, Integer> pointColumn;
    @FXML
    private TableColumn<Player, PlayerStatus> statusColumn;

    private ObservableList<Player> playerList;
    private ServerConnection serverConnection;
    private Stage primaryStage;  // Biến lưu trữ Stage chính
    private boolean isListening = false;

    // Thiết lập ServerConnection và Stage
    public void setServerConnection(ServerConnection serverConnection, Stage stage) {
        this.serverConnection = serverConnection;
        this.primaryStage = stage;  // Lưu Stage để sử dụng sau này

        // Bắt đầu lắng nghe từ server nếu chưa làm
        if (!isListening) {
            startListeningToServer();
        }
    }

    // Khởi động luồng lắng nghe từ server
    public void startListeningToServer() {
        isListening = true;

        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = serverConnection.receiveMessage();  // Nhận tin nhắn từ server
                    if (serverMessage != null) {
                        System.out.println("Received from server: " + serverMessage);
                        if (serverMessage.contains("Invite You Game")) {
                            System.out.println("You've received an invitation to play a game!");
                            moveInvitation();  // Chuyển đến màn hình mời
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }

    // Chuyển đến màn hình phòng chờ (WaitingRoom.fxml)
    private void moveToWaitingRoom() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));
                Parent waitViewParent = loader.load();

                // Lấy controller của phòng chờ và truyền serverConnection
                WaitingRoomController waitingRoomController = loader.getController();
                waitingRoomController.setServerConnection(serverConnection,primaryStage);

                Scene scene = new Scene(waitViewParent);
                primaryStage.setScene(scene);  // Sử dụng primaryStage để chuyển Scene
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Chuyển đến màn hình mời (Invitation.fxml)
    private void moveInvitation() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/n19/ltmproject/Invitation.fxml"));
                Parent invitationParent = loader.load();

                // Lấy controller của màn hình mời và truyền serverConnection
                InvitationController inviteController = loader.getController();
                inviteController.setServerConnection(serverConnection,primaryStage);

                Scene scene = new Scene(invitationParent);
                primaryStage.setScene(scene);  // Sử dụng primaryStage để chuyển Scene
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Xử lý sự kiện đăng xuất
    public void ClickLogout(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
        Parent loginViewParent = loader.load();
        Scene scene = new Scene(loginViewParent);
        stage.setScene(scene);
    }
    public void ClickThanhtuu(ActionEvent e) throws IOException {
//        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/ThanhTuu.fxml"));

        Parent ThanhTuuViewParent = loader.load();
        Scene scene = new Scene(ThanhTuuViewParent);

        // Truyền lại serverConnection về TrangChuController
        ThanhTuuController thanhtuuController = loader.getController();
        thanhtuuController.setServerConnection(serverConnection,primaryStage);

        primaryStage.setScene(scene);
    }

    // Xử lý sự kiện mời người chơi
    public void ClickInvitePlayer(ActionEvent event) throws IOException {
        // Lấy người chơi được chọn
        Player selectedPlayer = table.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            // Gửi lời mời đến người chơi được chọn qua server
            serverConnection.sendMessage("Invite:" + selectedPlayer.getUsername());

            // Chuyển người mời đến phòng chờ
            moveToWaitingRoom();
        } else {
            // Hiển thị thông báo nếu chưa chọn người chơi
            System.out.println("Vui lòng chọn người chơi để mời!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo danh sách người chơi
        playerList = FXCollections.observableArrayList(
            new Player("user1", "1", "chinh@gmail.com"),
            new Player("user2", "1", "chinh@gmail.com"),
            new Player("user3", "1", "chinh@gmail.com"),
            new Player("user4", "1", "chinh@gmail.com"),
            new Player("user5", "1", "chinh@gmail.com"),
            new Player("user6", "1", "chinh@gmail.com")
        );

        // Thiết lập các cột trong TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("username"));
        pointColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("totalPoints"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Player, PlayerStatus>("status"));

        table.setItems(playerList);
        table.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);

        // Lắng nghe sự kiện chọn hàng trong TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Hàng đã được chọn: " + newSelection.getUsername());
            }
        });
    }
}
