package com.n19.ltmproject.client.controller;

import java.io.IOException;

import com.n19.ltmproject.client.model.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamePlayController {

	private ServerConnection serverConnection;
	private Stage primaryStage;

	public void setServerConnection(ServerConnection serverConnection, Stage stage) {
		this.serverConnection = serverConnection;
		this.primaryStage = stage;
	}

	public void ClickExit(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

		Parent trangChuViewParent = loader.load();
		Scene scene = new Scene(trangChuViewParent);

		MainPageController mainPageController = loader.getController();
		mainPageController.setServerConnection(serverConnection,primaryStage);

		primaryStage.setScene(scene);
	}
}
