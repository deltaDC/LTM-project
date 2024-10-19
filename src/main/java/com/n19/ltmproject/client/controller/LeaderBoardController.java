package com.n19.ltmproject.client.controller;
// GET BXH
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.n19.ltmproject.client.handler.ServerHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.n19.ltmproject.client.model.Player;

public class LeaderBoardController implements Initializable {

    @FXML
    private TableView<Player> rankboard;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, String> matchColumn;

    @FXML
    private TableColumn<Player, Integer> rankColumn;

    @FXML
    private TableColumn<Player, Integer> winColumn;

    @FXML
    private TableColumn<Player, Integer> drawColumn;

    @FXML
    private TableColumn<Player, Integer> lossColumn;

    @FXML
    private TableColumn<Player, Integer> pointColumn;

    private ObservableList<Player> playerList;
    private final ServerHandler serverHandler = ServerHandler.getInstance();
    private Stage primaryStage;


    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void clickHome(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/n19/ltmproject/MainPage.fxml"));

        Parent MainPageViewParent = loader.load();
        Scene scene = new Scene(MainPageViewParent);

        MainPageController mainPageController = loader.getController();
        mainPageController.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO fetch from server
        playerList = FXCollections.observableArrayList(
//                new Player(1,"user1", "1", "chinh@gmail.com",49,20,15,4,1,PlayerStatus.OFFLINE),
//                new Player(2,"user2", "1", "chinh@gmail.com",45,20,14,3,3,PlayerStatus.OFFLINE),
//                new Player(3,"user3", "1", "chinh@gmail.com",41,20,12,5,3,PlayerStatus.OFFLINE),
//                new Player(4,"user4", "1", "chinh@gmail.com",36,20,10,6,4,PlayerStatus.OFFLINE),
//                new Player(5,"user5", "1", "chinh@gmail.com",32,20,9,5,6,PlayerStatus.OFFLINE)
        );

        rankColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        matchColumn.setCellValueFactory(new PropertyValueFactory<>("totalGames"));
        winColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
        drawColumn.setCellValueFactory(new PropertyValueFactory<>("draws"));
        lossColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        rankboard.setItems(playerList);
        rankboard.setFocusTraversable(false);
        rankboard.getSelectionModel().clearSelection();
    }
}
