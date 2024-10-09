package com.n19.ltmproject.client.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController {

	public void ClickLogin(MouseEvent e) throws IOException {
		Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
		
		Parent loginViewParent = loader.load();
		Scene scene = new Scene(loginViewParent);
		stage.setScene(scene);
	}

	public void ClickSignUp(ActionEvent e) throws IOException {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Signup");
		alert.setContentText("Sign Up Success");
		alert.showAndWait();
		
		Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
		
		Parent loginViewParent = loader.load();
		Scene scene = new Scene(loginViewParent);
		stage.setScene(scene);
	}
}
