package com.n19.ltmproject.client.controller;

import java.io.IOException;
import java.util.*;

import com.n19.ltmproject.client.handler.ServerHandler;
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
    private UserSession usersessions;
    private Session session;
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage,Session session, UserSession usersessions) {
        this.primaryStage = stage;
        this.session = session;
        this.usersessions = usersessions;
    }

    @FXML
    public void ClickLogin() {
        String username = userText.getText();
        String password = passText.getText();
        session.setUserID(username);
        session.setUsername(username);
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        Response response = messageService.sendRequest("login", params);
        System.out.println(response);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            Map<String, Object> responseData = (Map<String, Object>) response.getData();

// Lấy danh sách người dùng đang hoạt động và người dùng hiện tại
            Set<String> activeUsers = new HashSet<>((List<String>) responseData.get("activeUsers"));
            String currentUser = (String) responseData.get("currentUser");

            usersessions.setActiveUsers(activeUsers);

            showInformationAlert("Login", "Login successful!");

            try {
                loadMainPage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            showErrorAlert("Login", "Login failed. Please check your credentials.");
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

    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadMainPage() throws IOException {
        Stage stage = (Stage) userText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
        Parent root = loader.load();

        MainPageController mainpageController = loader.getController();
        mainpageController.setPrimaryStage( stage,session, usersessions);
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
