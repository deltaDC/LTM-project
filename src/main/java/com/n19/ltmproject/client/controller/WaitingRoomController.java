package com.n19.ltmproject.client.controller;

// CLICK STARTGAME ( XAC NHAN DA READY CHUA)


//  CAN THEM 1 LISTENING TUONG TU MAINPAGE DE LANG NGHE SU KIEN USER2 VAO PHONG
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.n19.ltmproject.client.handler.ServerHandler;

public class WaitingRoomController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;
    private volatile boolean running = true;
    @FXML
    private Label waitingroomnamechuphong;
    @FXML
    private Label waitingroomnamenguoichoi;
    public void setUpChuPhong(String waitingroomnamechuphong){
        this.waitingroomnamechuphong.setText(waitingroomnamechuphong);
        this.waitingroomnamenguoichoi.setText("Waiting");
    }
    public void setUpNguoiChoi(String chuphong,String waitingroomnamenguoichoi){
        this.waitingroomnamechuphong.setText(chuphong);
        this.waitingroomnamenguoichoi.setText(waitingroomnamenguoichoi);
    }

    public void setPrimaryStage(Stage stage) {

        this.primaryStage = stage;
//        running = true;
//        startListeningForInvite();
    }
//    private void startListeningForInvite() {
//        new Thread(() -> {
//            try {
//                while (running) {
//                    String serverMessage = serverHandler.receiveMessage();
//                    System.out.println(serverMessage);
//                    if (serverMessage != null && "playgamenow".equals(serverMessage)) {
//                        running=false;
////                        String idMatch=serverConnection.receiveMessage();
////                        String player1=serverConnection.receiveMessage();
////                        String player2=serverConnection.receiveMessage();
//
//                        Platform.runLater(() -> {
//                            try {
////                                Match newMatch=new Match(Integer.parseInt(idMatch), player1, player2, timeBegin);
//                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));
//                                Scene scene = new Scene(loader.load());
//                                GamePlayController gController = loader.getController();
//                                gController.setPrimaryStage(primaryStage);
////                                gController.setUser(dataUser);
////                                gController.setMatch(newMatch);
////                                gController.startCountdown();
//                                // Lấy stage hiện tại từ nút "exit"
////                                Stage stage = (Stage) exit.getScene().getWindow();
//                                primaryStage.setScene(scene);
//                                primaryStage.setTitle("Giao diện Game");
//                                primaryStage.show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//                }
//            } catch (IOException ex) {
//                System.out.println("Error receiving message from server: " + ex.getMessage());
//            }
//        }).start();
//    }

    public void ClickExit() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

            Parent MainPageViewParent = loader.load();
            Scene scene = new Scene(MainPageViewParent);

            MainPageController mainPageController = loader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            primaryStage.setScene(scene);
            mainPageController.setup2();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClickStart(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/GamePlay.fxml"));

        Parent GamePlayViewParent = loader.load();
        Scene scene = new Scene(GamePlayViewParent);

        GamePlayController gameController = loader.getController();
        gameController.setStage(primaryStage);

        primaryStage.setScene(scene);
    }

}

