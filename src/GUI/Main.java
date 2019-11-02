package GUI;

import Apk.Airport;
import GUI.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage paPrimaryStage) throws Exception{
        primaryStage = paPrimaryStage;
        primaryStage.setTitle("Airport");
        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("Enter date and time");
        vBox.getChildren().add(label);

        DateTimePicker dateTimePicker = new DateTimePicker();
        vBox.getChildren().add(dateTimePicker);

        Button createNewBtn = new Button(" Create new airport  ");
        createNewBtn.setOnAction(e -> createNewAirport(dateTimePicker.getDateTimeValue()));
        vBox.getChildren().add(createNewBtn);

        Button openExistingBtn = new Button("Open existing airport");
        openExistingBtn.setOnAction(e -> openExistingAirport());
        vBox.getChildren().add(openExistingBtn);

        Button openRunwaysFileBtn = new Button("    Edit runways file    ");
        openRunwaysFileBtn.setOnAction(e -> openRunwaysFile());
        vBox.getChildren().add(openRunwaysFileBtn);
        Scene scene = new Scene(vBox, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createNewAirport(LocalDateTime localDateTime)  {
        //System.out.println(localDateTime);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/MainView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            mainController.setAirport(new Airport(localDateTime));
            primaryStage.setScene(new Scene(root, 1000, 650));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openRunwaysFile() {
        try {
            File runways = new File("src\\Apk\\runways.csv");
            Desktop.getDesktop().open(runways);
        }
         catch (IOException io) {
            io.printStackTrace();
         }

    }

    private static void openExistingAirport() {
        //TODO, nacitanie vsetkych dat zo suboru
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
