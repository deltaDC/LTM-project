package com.n19.ltmproject.client.controller;
// CLICK EXIT
// SEND KET QUA TRAN DAU (UPDATE WIN , LOSS)
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GamePlayController {

    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    @FXML
    private ImageView trashImage;

    @FXML
    private ImageView trashCan1;
    @FXML
    private ImageView trashCan2;
    @FXML
    private ImageView trashCan3;
    @FXML
    private ImageView trashCan4;
    @FXML
    private ImageView trashCan5;

    //TODO rename for better readability
    @FXML
    private ImageView humanGameplay;
    @FXML
    private ImageView messageGameplay;
    @FXML
    private Label feedbackLabel;

    //TODO rename for better readability
    @FXML
    private ImageView humanGameplay1;
    @FXML
    private ImageView messageGameplay1;
    @FXML
    private Label feedbackLabel1;

    @FXML
    private Label scoreUser1;
    @FXML
    private Label scoreUser2;

    @FXML
    private Label timerLabel;

    private boolean isListening = false;

    private final Random random = new Random();
    private final int[] trashImagesCount = {21, 22};

	Timeline timeline;

    @Getter
    private Stage primaryStage;

    // Player's score
    @Getter
    private int score = 0;
    // Opponent's score
    @Getter
    private int opponentScore = 0;

    private int timeLeft = 10;

    private final String[] trashTypes = {"organic", "metal"};
    private final String[] correctFeedback = {"Correct!", "Nice!", "Good job!"};
    private final String[] incorrectFeedback = {"Wrong!", "Try next time!"};
    private String currentTrashType;

    private boolean isDragging = false;
    private double offsetX;
    private double offsetY;
    private double initialX;
    private double initialY;

    public void setPrimaryStage(Stage stage) {

        this.primaryStage = stage;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
        startGame();
        startListeningToServer();
    }

    private void startGame() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        spawnTrash();
    }

    private void startListeningToServer() {
        isListening = true;

        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = serverHandler.receiveMessage();
                    if (serverMessage != null) {
                        System.out.println("Received from server: " + serverMessage);
                        if (serverMessage.startsWith("updateScore")) {
                            String[] parts = serverMessage.split(":");
                            //TODO refactor this to use a more readable way
                            if (parts.length == 3) {
                                int updatedOpponentScore = Integer.parseInt(parts[2].trim());
                                updateOpponentScore(updatedOpponentScore);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error receiving message from server: " + ex.getMessage());
            }
        }).start();
    }

    private void updateOpponentScore(int updatedScore) {
        this.opponentScore = updatedScore;
        scoreUser2.setText("Score: " + opponentScore);  // Cập nhật hiển thị điểm cho người chơi đối thủ
        System.out.println("Opponent's score updated: " + opponentScore);
    }

    void updateTimer() {
        if (timeLeft > 0) {
            timeLeft--;
            timerLabel.setText("Thời gian còn lại: " + timeLeft + " giây");
        } else {
            endGame();
        }
    }

    private void spawnTrash() {
        int randomTypeIndex = random.nextInt(trashTypes.length);
        currentTrashType = trashTypes[randomTypeIndex];

        int maxImages = trashImagesCount[randomTypeIndex];
        int randomImageIndex = random.nextInt(maxImages) + 1;
        String imageDirectory = "/com/n19/ltmproject/images/";
        String imagePath = imageDirectory + currentTrashType + "/" + currentTrashType + randomImageIndex + ".png";

        try {
            Image trashImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            trashImage.setImage(trashImg);
        } catch (Exception e) {
            System.out.println("Lỗi: Không thể tải hình ảnh từ " + imagePath);
        }
        resetTrashPosition();
    }

    @FXML
    private void initialize() {
        trashImage.setOnMousePressed(event -> {
            isDragging = true;
            offsetX = event.getSceneX() - trashImage.getLayoutX();
            offsetY = event.getSceneY() - trashImage.getLayoutY();
            initialX = trashImage.getLayoutX();
            initialY = trashImage.getLayoutY();
        });

        trashImage.setOnMouseDragged(event -> {
            if (isDragging) {
                trashImage.setLayoutX(event.getSceneX() - offsetX);
                trashImage.setLayoutY(event.getSceneY() - offsetY);
            }
        });

        trashImage.setOnMouseReleased(event -> {
            isDragging = false;
            checkTrashDrop(event.getSceneX(), event.getSceneY());
        });

        trashCan1.setOnMouseClicked(event -> handleTrashDrop("glass"));
        trashCan2.setOnMouseClicked(event -> handleTrashDrop("metal"));
        trashCan3.setOnMouseClicked(event -> handleTrashDrop("organic"));
        trashCan4.setOnMouseClicked(event -> handleTrashDrop("paper"));
        trashCan5.setOnMouseClicked(event -> handleTrashDrop("plastic"));
    }

    private void checkTrashDrop(double dropX, double dropY) {
        Map<String, ImageView> trashCanMap = Map.of(
                "glass", trashCan1,
                "metal", trashCan2,
                "organic", trashCan3,
                "paper", trashCan4,
                "plastic", trashCan5
        );

        for (Map.Entry<String, ImageView> entry : trashCanMap.entrySet()) {
            if (isWithinBounds(dropX, dropY, entry.getValue())) {
                handleTrashDrop(entry.getKey());
                return;
            }
        }

        resetTrashPosition();
    }

    private boolean isWithinBounds(double dropX, double dropY, ImageView trashCan) {
        double canX = trashCan.getLayoutX();
        double canY = trashCan.getLayoutY();
        double canWidth = trashCan.getFitWidth();
        double canHeight = trashCan.getFitHeight();

        return dropX >= canX && dropX <= canX + canWidth && dropY >= canY && dropY <= canY + canHeight;
    }

    private void resetTrashPosition() {
        trashImage.setLayoutX(250);
        trashImage.setLayoutY(180);
    }

    private void handleTrashDrop(String binType) {
        if (currentTrashType.equals(binType)) {
            score++;
            scoreUser1.setText("Score: " + score);
            showFeedback(true);
            sendScoreUpdateToServer(score, opponentScore);
        } else {
            showFeedback(false);
        }
        spawnTrash();
    }

    private void showFeedback(boolean isCorrect) {
        // Set visibility based on whether the feedback is correct
        humanGameplay.setVisible(isCorrect);
        messageGameplay.setVisible(isCorrect);
        humanGameplay1.setVisible(!isCorrect);
        messageGameplay1.setVisible(!isCorrect);

        // Update feedback labels
        Label activeFeedbackLabel = isCorrect ? feedbackLabel : feedbackLabel1;
        Label inactiveFeedbackLabel = isCorrect ? feedbackLabel1 : feedbackLabel;
        String[] feedbackMessages = isCorrect ? correctFeedback : incorrectFeedback;
        String color = isCorrect ? "green" : "red";

        activeFeedbackLabel.setText(feedbackMessages[random.nextInt(feedbackMessages.length)]);
        activeFeedbackLabel.setStyle("-fx-text-fill: " + color + ";");
        inactiveFeedbackLabel.setText("");
    }


    private void sendScoreUpdateToServer(int scoreUser1, int scoreUser2) {
        Map<String, Object> params = new HashMap<>();
        params.put("scoreUser1", scoreUser1);
        params.put("scoreUser2", scoreUser2);


        Response response = messageService.sendRequest("updateScore", params);

        if (response != null && "OK".equals(response.getStatus())) {
            System.out.println("Score updated on server successfully.");
        } else {
            System.out.println("Failed to update score on server.");
        }
    }

    private void endGame() {
        timeline.stop();
        showResultScreen();
    }

    private void showResultScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Result.fxml"));
            Parent resultScreen = loader.load();
            ResultController resultController = loader.getController();

            String resultMessage;
            boolean isWin = score > opponentScore;
            boolean isDraw = score == opponentScore;
            String scoreMessage = score + " - " + opponentScore;

            // Set result message and call setResults based on game outcome
            if (isWin) {
                resultMessage = "Bạn đã thắng!";
            } else if (isDraw) {
                resultMessage = "Trận đấu hòa!";
            } else {
                resultMessage = "Bạn đã thua!";
            }

            resultController.setResults(resultMessage, scoreMessage, isWin, isDraw, "Đối thủ");

            Stage stage = (Stage) timerLabel.getScene().getWindow();
            stage.setScene(new Scene(resultScreen));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleExit() {
        showExitBattleModal();
    }

    private void showExitBattleModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/ExitBattle.fxml"));
            Parent exitBattleModal = loader.load();
            ExitBattleController exitBattleController = loader.getController();
            exitBattleController.setGamePlayController(this);

            Stage modalStage = new Stage();
            modalStage.initOwner(primaryStage);
            modalStage.setScene(new Scene(exitBattleModal));
            modalStage.setTitle("Kết thúc trận đấu");

            modalStage.setOnCloseRequest(event -> {
                event.consume();
                modalStage.hide();
            });

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
