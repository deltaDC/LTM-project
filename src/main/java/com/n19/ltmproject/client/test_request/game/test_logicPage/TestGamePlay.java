package com.n19.ltmproject.client.test_request.game.test_logicPage;

import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.n19.ltmproject.client.controller.GamePlayController;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class TestGamePlay extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));
        Scene scene = new Scene(loader.load());
        UserSession usersessions=new UserSession();
        Session session = new Session();
        GamePlayController gamePlayController = loader.getController();
        gamePlayController.setPrimaryStage(primaryStage,session,usersessions);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Test GamePlay");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
