package com.n19.ltmproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExampleController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}