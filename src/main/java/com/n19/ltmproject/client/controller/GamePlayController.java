package com.n19.ltmproject.client.controller;
// CLICK EXIT
// SEND KET QUA TRAN DAU (UPDATE WIN , LOSS)
import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class GamePlayController {

	@FXML
	private AnchorPane trashCanPane;

	@FXML
	private Label scoreLabel;

	@FXML
	private Label timerLabel;

	@FXML
	private Button exitButton;

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

	private ServerHandler serverConnection;
	private Stage primaryStage;

	private int score;
	private int timeLeft;
	private boolean isGameActive;

	private String[] trashTypes = {"organic", "plastic", "metal", "paper", "glass"};
	private Random random = new Random();
	private String imageDirectory = "/com/n19/ltmproject/images/";
	private int[] trashImagesCount = {21, 30, 25, 18, 20};
	private String currentTrashType;

	public void setServerConnection(ServerHandler serverHandler, Stage stage) {
		this.serverConnection = serverHandler;
		this.primaryStage = stage;
		startGame();
	}

	private void startGame() {
		score = 0;
		timeLeft = 120;
		isGameActive = true;

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

		Image trashImg = new Image(getClass().getResourceAsStream(imagePath));
		if (trashImg.isError()) {
			System.out.println("Lỗi: Không thể tải hình ảnh từ " + imagePath);
		} else {
			trashImage.setImage(trashImg);
			System.out.println("Hình ảnh rác được tải: " + imagePath);
		}
	}

	@FXML
	private void initialize() {
		// Bắt sự kiện chuột cho hình ảnh rác
		trashImage.setOnDragDetected(event -> {
			Dragboard db = trashImage.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putImage(trashImage.getImage());
			db.setContent(content);
			event.consume();
		});

		// Bắt sự kiện cho các thùng rác
		initializeTrashCanDragEvents(trashCan1, "glass");
		initializeTrashCanDragEvents(trashCan2, "metal");
		initializeTrashCanDragEvents(trashCan3, "organic");
		initializeTrashCanDragEvents(trashCan4, "paper");
		initializeTrashCanDragEvents(trashCan5, "plastic");
	}

	private void initializeTrashCanDragEvents(ImageView trashCan, String binType) {
		trashCan.setOnDragOver(event -> {
			if (event.getGestureSource() != trashImage && event.getDragboard().hasImage()) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			event.consume();
		});

		trashCan.setOnDragDropped(event -> {
			boolean isCorrect = checkTrashType(binType);
			handleTrashDrop(isCorrect);
			event.setDropCompleted(isCorrect);
			event.consume();
		});
	}

	private void handleTrashDrop(boolean isCorrect) {
		if (isCorrect) {
			score++;
			scoreLabel.setText("Điểm: " + score);
		} else {
			score--;
			scoreLabel.setText("Điểm: " + score);
		}

		// Xuất hiện rác mới
		spawnTrash();
	}

	private boolean checkTrashType(String binType) {
		return currentTrashType.equals(binType);
	}

	private void endGame() {
		isGameActive = false;
		try {
			serverConnection.sendMessage("Game over. Điểm của bạn: " + score);
			boolean isWinner = score > 50;

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/n19/ltmproject/Result.fxml"));
			Parent endGameView = loader.load();
			Scene scene = new Scene(endGameView);

			ResultController resultController = loader.getController();
			String results = "Điểm của bạn: " + score;
			resultController.setResults(results, isWinner);

			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handleExit(ActionEvent event) throws IOException {
		serverConnection.sendMessage("Player exited the game.");
		primaryStage.close();
	}
}
