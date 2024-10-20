package com.n19.ltmproject.client.controller;
// CLICK EXIT
// SEND KET QUA TRAN DAU (UPDATE WIN , LOSS)
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
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
import java.util.Map;
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
	Label scoreUser1;
	@FXML
    Label scoreUser2;

	@FXML
	Label timerLabel;

	private final Random random = new Random();
	private final int[] trashImagesCount = {21};

	private final ServerHandler serverHandler = ServerHandler.getInstance();
	private Stage primaryStage;
	private UserSession usersessions;
	private Session session;
	private int score = 0;
	private int opponentScore = 0;
	private int timeLeft = 5;
	private final boolean isGameActive = true;
	Timeline timeline;

	private final String[] trashTypes = {"organic"};
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

		startGame();
	}


	private void startGame() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		spawnTrash();
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
		timeline.stop();

		boolean isWinner = score > opponentScore;
		boolean isDraw = score == opponentScore;
		String resultMessage = isDraw ? "Hòa!" : (isWinner ? "Bạn đã thắng!" : "Bạn đã thua!");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Result.fxml"));
			Parent resultView = loader.load();
			Scene scene = new Scene(resultView);

			ResultController resultController = loader.getController();
			String results = "Điểm của bạn: " + score + "\nĐiểm đối thủ: " + opponentScore;
			resultController.setResults(results, isWinner, isDraw, "Đối thủ");

			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: Unable to load Result.fxml");
		}
	}

	private void sendResultToServer(String result) {
		try {
			// Giả sử serverConnection có phương thức send để gửi dữ liệu
//			serverConnection.send("Kết quả trận đấu: " + result + ", Điểm của bạn: " + score + ", Điểm đối thủ: " + opponentScore);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi: Không thể gửi kết quả về server");
		}
	}

	@FXML
	private void handleExit() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/ExitBattle.fxml"));
			Parent exitBattleView = loader.load();
			Scene exitScene = new Scene(exitBattleView);

			ExitBattleController exitController = loader.getController();
			exitController.setGamePlayState(score, opponentScore, timeLeft, timeline, primaryStage);

			primaryStage.setScene(exitScene);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: Unable to load ExitBattle.fxml");
		}
	}

	// Hàm để cập nhật thời gian sau khi quay lại từ trang ExitBattle
	public void setTimeLeft(int newTimeLeft) {
		this.timeLeft = newTimeLeft;
		timerLabel.setText("Thời gian còn lại: " + timeLeft + " giây");
	}

}
