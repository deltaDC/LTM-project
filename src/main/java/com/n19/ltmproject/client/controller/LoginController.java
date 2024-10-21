package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.Player;
import com.n19.ltmproject.client.model.auth.SessionManager;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;


import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField userText;
    @FXML
    private PasswordField passText;

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void ClickLogin() {
        String username = userText.getText();
        String password = passText.getText();

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        MainPageController.isListening = false;
        Response response = messageService.sendRequest("login", params);

        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            AlertController.showInformationAlert("Login", "Login successful!");

            // Lưu thông tin người dùng vào SessionManager
            Gson gson = new Gson();
            String playerJson = gson.toJson(response.getData());
            Player loggedInPlayer = gson.fromJson(playerJson, Player.class);

            SessionManager.setCurrentUser(loggedInPlayer);

            try {
                loadMainPage();
                MainPageController.isListening = true;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            AlertController.showErrorAlert("Login", "Login failed. Please check your credentials.");
        }
    }

    @FXML
    public void ClickSignUp() {
        try {
            loadScene("/com/n19/ltmproject/SignUp.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMainPage() throws IOException {
        Stage stage = (Stage) userText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
        Parent root = loader.load();

        MainPageController mainpageController = loader.getController();
        mainpageController.setPrimaryStage( stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadScene(String resourcePath) throws IOException {
        Stage stage = (Stage) userText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(loader.load()));
        newStage.show();
        stage.close();
    }
}
