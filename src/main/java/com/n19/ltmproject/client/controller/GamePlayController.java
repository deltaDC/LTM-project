package com.n19.ltmproject.client.controller;

import java.io.IOException;

import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamePlayController {

	private ServerHandler serverHandler;
	private Stage primaryStage;

	public void setServerConnection(ServerHandler serverHandler, Stage stage) {
		this.serverHandler = serverHandler;
		this.primaryStage = stage;
	}

	public void ClickExit(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

		Parent MainPageViewParent = loader.load();
		Scene scene = new Scene(MainPageViewParent);

		MainPageController mainPageController = loader.getController();
		mainPageController.setServerConnection(serverHandler, primaryStage);

		primaryStage.setScene(scene);
	}
}
