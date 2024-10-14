package com.n19.ltmproject.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import com.google.gson.Gson;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Request;
import com.n19.ltmproject.client.model.dto.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 1234;
	private static final Gson gson = new Gson();

	@FXML
	TextField userText ;
	@FXML
	PasswordField passText, confirmPassText;

	public void ClickLogin(MouseEvent e) throws IOException {
		Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));
		
		Parent loginViewParent = loader.load();
		Scene scene = new Scene(loginViewParent);
		stage.setScene(scene);
	}

	public void ClickSignUp(ActionEvent e) throws IOException {
		Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
		ServerHandler serverHandler = new ServerHandler();
		serverHandler.connect("localhost", 1234);

		String username = userText.getText();
		String password = passText.getText();
		String confirmPassword = confirmPassText.getText();

		Request request = new Request();
		request.setAction("signUp");
		Map<String, Object> params = Map.of("username", username, "password", password, "confirmPassword", confirmPassword);
		request.setParams(params);

		String toJson = gson.toJson(request);
		output.println(toJson);

		String jsonResponse = input.readLine();
		Response response = gson.fromJson(jsonResponse, Response.class);

		System.out.println("Response from server: " + response.getMessage());

		if (response.getMessage().contains("Đăng ký thành công!")) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Signup");
			alert.setAlertType(Alert.AlertType.INFORMATION);
			alert.setContentText("Đăng ký thành công!");
			alert.showAndWait();

			// Điều hướng về trang đăng nhập sau khi đăng ký thành công
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/n19/ltmproject/Login.fxml"));

			Parent loginViewParent = loader.load();
			Scene scene = new Scene(loginViewParent);
			stage.setScene(scene);
		}
		else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Signup");
			alert.setAlertType(Alert.AlertType.ERROR);
			alert.setContentText(response.getMessage());
			alert.showAndWait();
		}
	}
}
