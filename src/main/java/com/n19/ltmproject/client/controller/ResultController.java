package com.n19.ltmproject.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ResultController {

    @FXML
    private TextArea scoreTextArea;  // Text area to display detailed results

    @FXML
    private Label resultLabel;  // Label to display general result (e.g., "You Won!", "Game Over")

    private boolean isWinner;  // Biến để kiểm tra xem người chơi có thắng hay không

    // This method is used to set the final results and display them on the end game screen.
    public void setResults(String results, boolean isWinner) {
        this.isWinner = isWinner;  // Gán giá trị cho biến isWinner
        scoreTextArea.setText(results);
        if (isWinner) {
            resultLabel.setText("Bạn đã thắng!");
        } else {
            resultLabel.setText("Bạn đã thua!");
        }
    }

    // Called when the user clicks "Exit" after the game ends.
    @FXML
    private void handleExit() {
        // Close the game or navigate to the main menu
        Stage stage = (Stage) scoreTextArea.getScene().getWindow();
        stage.close();  // Close the current window
    }

    // Called when the user clicks "Play Again" after the game ends.
    @FXML
    private void handlePlayAgain() {
        // Logic to restart or navigate to a new game
        Stage stage = (Stage) scoreTextArea.getScene().getWindow();
        // Close the current window or restart the game (you can add logic to reload the game scene here)
        stage.close();
    }
}
