package com.n19.ltmproject.client.controller;
// ACCEPT INVITATION
// REFUSE INVITATION
import java.io.IOException;
import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InvitationController {

	private ServerHandler serverHandler;
	private Stage primaryStage;

	public void setServerConnection(ServerHandler serverHandler, Stage stage) {
        this.serverHandler = serverHandler;
        this.primaryStage = stage; 
	}

	public void ClickAccept(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/WaitingRoom.fxml"));

        Parent WaitingRoomParent = loader.load();
        Scene scene = new Scene(WaitingRoomParent);

        WaitingRoomController WaitingRoomParentController = loader.getController();
        WaitingRoomParentController.setServerConnection(serverHandler,primaryStage);
        
        primaryStage.setScene(scene);
    }

	public void ClickRefuse(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setServerConnection(serverHandler,primaryStage);
        
        primaryStage.setScene(scene);
    }

	public void ClickInviteX(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent trangChuViewParent = loader.load();
        Scene scene = new Scene(trangChuViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setServerConnection(serverHandler,primaryStage);
        
        primaryStage.setScene(scene);
    }
}
