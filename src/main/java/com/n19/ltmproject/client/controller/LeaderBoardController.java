package com.n19.ltmproject.client.controller;
// GET BXH
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.n19.ltmproject.client.handler.ServerHandler;
import com.n19.ltmproject.server.service.Session;
import com.n19.ltmproject.server.service.UserSession;
import com.n19.ltmproject.client.model.PlayerHistory;
import com.n19.ltmproject.client.model.dto.PlayerHistoryDto;
import com.n19.ltmproject.client.model.dto.Response;
import com.n19.ltmproject.client.service.MessageService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.Player;

public class LeaderBoardController implements Initializable {

    @FXML
    private TableView<PlayerHistoryDto> rankboard;

    @FXML
    private TableColumn<PlayerHistoryDto, String> nameColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, String> matchColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, Integer> rankColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, Integer> winColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, Integer> drawColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, Integer> lossColumn;

    @FXML
    private TableColumn<PlayerHistoryDto, Integer> pointColumn;

    private ObservableList<PlayerHistoryDto> playerList;
    private final Gson gson = new Gson();
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private final MessageService messageService = new MessageService(serverHandler);

    public void clickHome(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));
        Parent LeaderBoardViewParent = loader.load();
        Scene scene = new Scene(LeaderBoardViewParent);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPlayers();
    }

    private void loadPlayers() {
        try {
            Map<String, Object> params = Map.of();
            Response response = messageService.sendRequest("getAllPlayerHistory", params);

            Platform.runLater(() -> {
                if (response != null && "OK".equalsIgnoreCase(response.getStatus())) {
                    List<PlayerHistoryDto> playerHistoriesDto = gson.fromJson(new Gson().toJson(response.getData()), new TypeToken<List<PlayerHistoryDto>>() {}.getType());

                    playerList = FXCollections.observableArrayList(playerHistoriesDto);

                    rankColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(rankboard.getItems().indexOf(cellData.getValue()) + 1));
                    nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
                    matchColumn.setCellValueFactory(new PropertyValueFactory<>("totalGames"));
                    winColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
                    drawColumn.setCellValueFactory(new PropertyValueFactory<>("draws"));
                    lossColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));
                    pointColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

                    rankboard.setItems(playerList);
                    rankboard.setFocusTraversable(false);
                    rankboard.getSelectionModel().clearSelection();

                    rankboard.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            System.out.println("Selected items: " + newSelection.getUsername());
                        }
                    });
                } else {
                    System.out.println("Failed to get players: " + (response != null ? response.getMessage() : "Unknown error"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
