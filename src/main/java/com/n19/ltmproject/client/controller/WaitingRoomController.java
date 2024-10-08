package com.n19.ltmproject.client.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.ServerConnection;
public class WaitingRoomController {
    private ServerConnection serverConnection; // Thêm thuộc tính này để nhận serverConnection
    private Stage primaryStage;
    // Phương thức để set serverConnection từ TrangChuController
    public void setServerConnection(ServerConnection serverConnection, Stage stage) {
        this.serverConnection = serverConnection;
        this.primaryStage = stage; 
    }

    public void ClickExit(ActionEvent e) throws IOException {
//        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/TrangChu.fxml"));

        Parent trangChuViewParent = loader.load();
        Scene scene = new Scene(trangChuViewParent);

        // Truyền lại serverConnection về TrangChuController
        TrangChuController trangChuController = loader.getController();
        trangChuController.setServerConnection(serverConnection,primaryStage); 

        primaryStage.setScene(scene);
    }

    public void ClickStart(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent gameViewParent = loader.load();
        Scene scene = new Scene(gameViewParent);

        // Truyền lại serverConnection về TrangChuController
        GamePlayController gameController = loader.getController();
        gameController.setServerConnection(serverConnection,primaryStage);

        primaryStage.setScene(scene);
    }
}

