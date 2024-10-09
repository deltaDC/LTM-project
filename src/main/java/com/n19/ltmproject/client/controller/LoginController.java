package com.n19.ltmproject.client.controller;

import java.io.IOException;
import com.n19.ltmproject.client.model.ServerConnection;
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

    @FXML
    TextField userText,passText;

    public void ClickLogin(ActionEvent e) throws IOException {
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.connect("localhost", 1234);

        serverConnection.sendMessage(userText.getText());

        String response = serverConnection.receiveMessage();
        System.out.println("Phản hồi từ server: " + response);

        //TODO change hard code to enum
        if (response.contains("Login successful")) {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent trangChuViewParent = loader.load();
            Scene scene = new Scene(trangChuViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setServerConnection(serverConnection,stage);
            
            stage.setScene(scene);
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
