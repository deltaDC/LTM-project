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
    private ServerConnection serverConnection;
    @FXML
    TextField userText,passText;

    public void ClickLogin(ActionEvent e) throws IOException {
        // Kết nối tới server
        serverConnection = new ServerConnection();
        serverConnection.connect("localhost", 1234); // Địa chỉ IP và cổng của server

        // Gửi thông điệp đăng nhập tới server
        serverConnection.sendMessage(userText.getText());

        // Nhận phản hồi từ server
        String response = serverConnection.receiveMessage();
        System.out.println("Phản hồi từ server: " + response);

        // Nếu đăng nhập thành công
        if (response.contains("Login successful")) {
            // Chuyển sang trang chính sau khi đăng nhập thành công
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/n19/ltmproject/TrangChu.fxml"));

            Parent trangChuViewParent = loader.load();
            Scene scene = new Scene(trangChuViewParent);

            // Lấy controller và thiết lập kết nối server
            TrangChuController trangChuController = loader.getController();
            trangChuController.setServerConnection(serverConnection,stage);
            
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
