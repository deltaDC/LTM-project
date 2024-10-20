package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InvitationController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private UserSession usersessions;
    private Session session;
    private Timer inactivityTimer; // Timer để theo dõi thời gian không hoạt động
    private static final long INACTIVITY_LIMIT = 10000; // Thời gian không hoạt động (10 giây)
    private boolean isTaskCancelled = false; // Biến theo dõi trạng thái của task

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void initialize() {
        startInactivityTimer(); // Bắt đầu timer khi khởi tạo
    }

    private void startInactivityTimer() {
        inactivityTimer = new Timer();
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!isTaskCancelled) {
                        try {
                            moveToMainPage(); // Chuyển về MainPage nếu không có phản hồi
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, INACTIVITY_LIMIT); // 10 giây
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        isTaskCancelled = true; // Đánh dấu task đã bị hủy
        inactivityTimer.cancel(); // Hủy timer
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        isTaskCancelled = true; // Đánh dấu task đã bị hủy
        inactivityTimer.cancel(); // Hủy timer
        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }
    public void ClickInviteX(ActionEvent e) throws IOException {
        isTaskCancelled = true; // Đánh dấu task đã bị hủy
        inactivityTimer.cancel(); // Hủy timer
        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }

    private void moveToMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml")); // Đường dẫn đến FXML của Main Page

        Parent mainPageParent = loader.load();
        Scene scene = new Scene(mainPageParent);
        primaryStage.setScene(scene); // Cập nhật cảnh cho Primary Stage

        // Nếu cần, bạn có thể thêm mã để khởi tạo lại controller cho MainPage
        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage); // Truyền Primary Stage cho MainPageController
    }
}