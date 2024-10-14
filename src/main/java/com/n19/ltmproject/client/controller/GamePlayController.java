package com.n19.ltmproject.client.controller;

import com.n19.ltmproject.client.handler.ServerHandler;
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

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class GamePlayController {

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

	@FXML
	private Label feedbackLabel;

	@FXML
	private Label scoreUser1;
	@FXML
	private Label scoreUser2;

	@FXML
	private Label timerLabel;

	private Random random = new Random();
	private String imageDirectory = "/com/n19/ltmproject/images/";
//	private int[] trashImagesCount = {21, 30, 25, 18, 20};
	private int[] trashImagesCount = {21};


	private ServerHandler serverConnection;
	private Stage primaryStage;

	private int score = 0;
	private int opponentScore = 0;
	private int timeLeft = 120;
	private boolean isGameActive = true;

	private String[] trashTypes = {"organic"};
//	private String[] trashTypes = {"organic", "plastic", "metal", "paper", "glass"};
	private String[] correctFeedback = {"Correct!", "Nice!", "Good job!"};
	private String[] incorrectFeedback = {"Wrong!", "Try next time!"};
	private String currentTrashType;

	public void setServerConnection(ServerHandler serverHandler, Stage stage) {
		this.serverConnection = serverHandler;
		this.primaryStage = stage;
		startGame();
	}

	private void startGame() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		spawnTrash();
	}

	private void updateTimer() {
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
		String imagePath = imageDirectory + currentTrashType + "/" + currentTrashType + randomImageIndex + ".png";

		try {
			Image trashImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
			trashImage.setImage(trashImg);
			System.out.println("Hình ảnh rác được tải: " + imagePath);
		} catch (Exception e) {
			System.out.println("Lỗi: Không thể tải hình ảnh từ " + imagePath);
		}

	}


	@FXML
	private void initialize() {
		trashCan1.setOnMouseClicked(event -> handleTrashDrop("glass"));
		trashCan2.setOnMouseClicked(event -> handleTrashDrop("metal"));
		trashCan3.setOnMouseClicked(event -> handleTrashDrop("organic"));
		trashCan4.setOnMouseClicked(event -> handleTrashDrop("paper"));
		trashCan5.setOnMouseClicked(event -> handleTrashDrop("plastic"));
	}

	private void handleTrashDrop(String binType) {
		if (currentTrashType.equals(binType)) {
			score++;
			scoreUser1.setText("Score: " + score);
			showFeedback(true);
		} else {
			showFeedback(false);
		}
		spawnTrash();
	}

	private void showFeedback(boolean isCorrect) {
		if (isCorrect) {
			feedbackLabel.setText(correctFeedback[random.nextInt(correctFeedback.length)]);
			feedbackLabel.setStyle("-fx-text-fill: green;");
		} else {
			feedbackLabel.setText(incorrectFeedback[random.nextInt(incorrectFeedback.length)]);
			feedbackLabel.setStyle("-fx-text-fill: red;");
		}
	}

	public void updateOpponentScore(int newScore) {
		opponentScore = newScore;
		scoreUser2.setText("Score: " + opponentScore);
	}

	private void endGame() {
//		isGameActive = false;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Result.fxml"));
			Parent resultView = loader.load();
			Scene scene = new Scene(resultView);

			ResultController resultController = loader.getController();
			String results = "Điểm của bạn: " + score;
			resultController.setResults(results, score > opponentScore);

			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: Unable to load Result.fxml");
		}
	}

	@FXML
	private void handleExit() {
		primaryStage.close();
	}
}
