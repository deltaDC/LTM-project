package com.n19.ltmproject.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class TutorialController {
    @FXML
    private ImageView imageView;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Button homeButton;
    @FXML
    private VBox tutorialContainer;
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private final String[] imagePaths = {
            "/com/n19/ltmproject/images/tutorial/tutorial1.png",
            "/com/n19/ltmproject/images/tutorial/tutorial2.png",
            "/com/n19/ltmproject/images/tutorial/tutorial3.png",
            "/com/n19/ltmproject/images/tutorial/tutorial4.png",
            "/com/n19/ltmproject/images/tutorial/tutorial5.png",
            "/com/n19/ltmproject/images/tutorial/tutorial6.png",
            "/com/n19/ltmproject/images/tutorial/tutorial7.png"
    };

    private int currentIndex = 0;

    @FXML
    private void initialize() {
        showIntroductionPage();
    }

    private void showIntroductionPage() {
        tutorialContainer.getChildren().clear();

        Label introLabel = new Label("Luật chơi:\n" +
                "1. Thời gian: Trò chơi diễn ra trong 2 phút.\n" +
                "2. Phân loại rác: Các loại rác ngẫu nhiên sẽ xuất hiện kèm theo các thùng rác tương ứng.\n" +
                "   - Hữu cơ: Thùng màu xanh lá cây (thực phẩm, vỏ hoa quả...)\n" +
                "   - Nhựa: Thùng màu vàng (chai nhựa, túi nylon...)\n" +
                "   - Kim loại: Thùng màu xám (lon nhôm, hộp kim loại...)\n" +
                "   - Giấy: Thùng màu xanh dương (báo cũ, bìa carton...)\n" +
                "   - Thủy tinh: Thùng màu trắng (chai thủy tinh, ly thủy tinh...)\n" +
                "3. Điểm số: Mỗi lần phân loại đúng sẽ nhận 1 điểm, phân loại sai sẽ bị trừ 1 điểm.\n" +
                "4. Kết thúc trò chơi: Khi hết thời gian, người chơi có điểm cao hơn sẽ thắng.\n" +
                "5. Tổng kết điểm tích lũy: Trận thắng sẽ được 3 điểm, hòa được 1 điểm, thua được 0 điểm.\n" +
                "   Người chơi thoát trận sớm sẽ thua ngay lập tức.");

        introLabel.setWrapText(true);
        tutorialContainer.getChildren().add(introLabel);
        nextButton.setText("Tiếp tục");
        backButton.setVisible(false); // Ẩn nút Quay lại trong trang đầu tiên

        // Đặt sự kiện cho nút Next
        nextButton.setOnAction(e -> handleNextButtonAction());
        updateButtonVisibility();
    }

    @FXML
    private void handleNextButtonAction() {
        if (currentIndex < imagePaths.length) {
            if (currentIndex == 0) {
                tutorialContainer.getChildren().clear();
                tutorialContainer.getChildren().add(imageView);
            }
            displayImage(currentIndex);
            currentIndex++;
            if (currentIndex == imagePaths.length) {
                // Nếu đến trang cuối cùng
                showFinalPage();
            }
        }
        updateButtonVisibility();
    }

    @FXML
    private void handleBackButtonAction() {
        if (currentIndex > 0) {
            currentIndex--;
            if (currentIndex == 0) {
                showIntroductionPage();
            } else {
                displayImage(currentIndex);
            }
        }
        updateButtonVisibility();
    }



    private void displayImage(int index) {
        Image image = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(imagePaths[index])));
        imageView.setImage(image);
        nextButton.setText(index == imagePaths.length - 1 ? "Đã hiểu" : "Next");
    }

    private void showFinalPage() {
        tutorialContainer.getChildren().clear();
        Label tableLabel = new Label("Bảng phân loại:\n" +
                "- Rác hữu cơ: Thùng màu xanh lá cây.\n" +
                "- Rác kim loại: Thùng màu xám.\n" +
                "- Rác giấy: Thùng màu xanh dương.\n" +
                "- Rác nhựa: Thùng màu vàng.\n" +
                "- Rác thủy tinh: Thùng màu trắng.");
        tableLabel.setWrapText(true);
        tutorialContainer.getChildren().add(tableLabel);
        nextButton.setText("Đã hiểu");
        backButton.setVisible(false);

        nextButton.setOnAction(e -> {
            currentIndex = 0;
            try {
                goToMainPage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        backButton.setVisible(currentIndex > 0); // Hiện nút Quay lại nếu không phải trang đầu
        nextButton.setVisible(currentIndex < imagePaths.length); // Hiện nút Next nếu không phải trang cuối
    }

    @FXML
    private void goToMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        mainPageController.setupMainPage();
    }
}