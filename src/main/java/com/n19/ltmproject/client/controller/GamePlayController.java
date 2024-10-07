package com.n19.ltmproject.client.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamePlayController {
	public void ClickExit(ActionEvent e) throws IOException {
		Stage stage =(Stage) ((Node)e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/TrangChu.fxml"));
		
		Parent trangChuViewParent = loader.load();
		Scene scene = new Scene(trangChuViewParent);
		stage.setScene(scene);
	}
}
