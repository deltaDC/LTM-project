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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private static final Gson gson = new Gson();


    @FXML
    TextField userText,passText;

    public void ClickLogin(ActionEvent e) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
         BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
         ServerHandler serverHandler = new ServerHandler();
         serverHandler.connect("localhost", 1234);

         String username = userText.getText();
         String password = passText.getText();

//         serverHandler.sendMessage(username + " " + password);

         Request request = new Request();
         request.setAction("login");
         Map<String, Object> params = Map.of("username", username, "password", password);
         request.setParams(params);

         String toJson = gson.toJson(request);
         output.println(toJson);

         String jsonResponse = input.readLine();
         Response response = gson.fromJson(jsonResponse, Response.class);

         System.out.println("Response from server: " + response.getMessage());

         //TODO change hard code to enum
        if (response.getMessage().contains("Login successful")) {
            System.out.println("Login successful, transitioning to MainPage...");

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            System.out.println("MainPage.fxml loaded successfully.");

            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setServerConnection(serverHandler, stage);
            System.out.println("MainPageController setup completed.");

            stage.setScene(scene);
            System.out.println("Scene changed to MainPage.");
        } else {
            System.out.println("Login failed: " + response);
        }
    }

    public void ClickSignUp(MouseEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/SignUp.fxml"));

        Parent SignUpParent = loader.load();
        Scene scene = new Scene(SignUpParent);
        stage.setScene(scene);
    }
}
