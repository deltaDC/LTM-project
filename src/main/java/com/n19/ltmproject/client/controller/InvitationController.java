package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION
import java.io.IOException;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
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
    public void setPrimaryStage(Stage stage, Session session, UserSession usersessions) {
        this.primaryStage = stage;
        this.session = session;
        this.usersessions = usersessions;
    }

	public void ClickAccept(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setPrimaryStage(primaryStage,session,usersessions);
        
        primaryStage.setScene(scene);
    }

	public void ClickRefuse(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage,session,usersessions);
        
        primaryStage.setScene(scene);
    }

	public void ClickInviteX(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent trangChuViewParent = loader.load();
        Scene scene = new Scene(trangChuViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage,session,usersessions);
        
        primaryStage.setScene(scene);
    }
}
