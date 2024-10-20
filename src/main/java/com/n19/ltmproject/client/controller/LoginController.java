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
    public static UserSession usersessions = new UserSession();
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
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
//        if (usersessions != null) {
//            params.put("usersession", usersessions);
//        } else {
//            System.out.println("usersessions is null");
//        }

        Response response = messageService.sendRequest("login", params);
        System.out.println(response);
        if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
            // Lấy dữ liệu từ response
            Map<String, Object> responseData = (Map<String, Object>) response.getData();

//            // Kiểm tra xem responseData có phải là Map không
//            if (responseData instanceof Map) {
//                // Lấy UserSession từ responseData
//                Object userSessionObject = responseData.get("usersession");
//                if (userSessionObject instanceof UserSession) {
//                    UserSession sessionData = (UserSession) userSessionObject;
//                    usersessions.setSession(sessionData);
//
//                }


                // Lấy Player từ responseData
                Object playerObject = responseData.get("player");
                if (playerObject != null) {
                    // Chuyển đổi playerObject thành Player
                    Gson gson = new Gson();
                    String playerJson = gson.toJson(playerObject);
                    Player loggedInPlayer = gson.fromJson(playerJson, Player.class);
                    SessionManager.setCurrentUser(loggedInPlayer);
                }


//            usersessions.addSession(username);
            AlertController.showInformationAlert("Login", "Login successful!");

            try {
                loadMainPage();
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
