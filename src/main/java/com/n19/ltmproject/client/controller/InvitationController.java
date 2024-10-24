package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InvitationController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private Timeline timeline;

    @FXML
    private Button invitation1;
    @FXML
    private Label invitationprofile;
    private String nguoimoi;
    public void setUpInvitation(String invitation1,String invitationprofile){
        this.nguoimoi = invitation1;
        this.invitation1.setText(invitation1.toUpperCase() + " INVITE YOU");
        this.invitationprofile.setText(invitationprofile);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void ClickAccept(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop(); // Dừng việc tự động quay về trang chính
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setPrimaryStage(primaryStage);
        WaitingRoomParentController.setUpNguoiChoi(this.nguoimoi, SessionManager.getCurrentUser().getUsername());

        primaryStage.setScene(scene);
    }

    public void ClickRefuse(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop(); // Dừng việc tự động quay về trang chính
        }
        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }

    public void ClickInviteX(ActionEvent e) throws IOException {
        if (timeline != null) {
            timeline.stop(); // Dừng việc tự động quay về trang chính
        }
        moveToMainPage(); // Chuyển về MainPage ngay lập tức
    }

    private void moveToMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        // de cho thread bat null ( bug nho )
        mainPageController.setup2();
    }
}