package GUI;

import Apk.Airport;
import GUI.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;

public class Main extends Application {

    private static Stage primaryStage;
    private static File runwaysFile;

    @Override
    public void start(Stage paPrimaryStage) throws Exception{
        try {
            //System.out.println(new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/GUI/numberOfRunways.csv"));
            File actualFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            runwaysFile =  new File(actualFile.getParent() + "/numberOfRunways.csv");
            System.out.println(runwaysFile);
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File with runways not found.");
            alert.showAndWait();
            return;
        }

        primaryStage = paPrimaryStage;
        primaryStage.setTitle("Airport");
        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("Enter date and time");
        vBox.getChildren().add(label);

        DateTimePicker dateTimePicker = new DateTimePicker();
        vBox.getChildren().add(dateTimePicker);

        Button createNewBtn = new Button("Create new airport");
        createNewBtn.setOnAction(e -> createNewAirport(dateTimePicker.getDateTimeValue()));
        vBox.getChildren().add(createNewBtn);

        Button openExistingBtn = new Button("Open existing airport");
        openExistingBtn.setOnAction(e -> openExistingAirport());
        vBox.getChildren().add(openExistingBtn);

        Button openRunwaysFileBtn = new Button("Edit runways file");
        openRunwaysFileBtn.setOnAction(e -> openRunwaysFile());
        vBox.getChildren().add(openRunwaysFileBtn);
        Scene scene = new Scene(vBox, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();


    }



    private void createNewAirport(LocalDateTime localDateTime)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/MainView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            mainController.setAirport(new Airport(localDateTime, runwaysFile));
            primaryStage.setScene(new Scene(root, 1000, 650));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void openRunwaysFile() {
        try {
            Desktop.getDesktop().open(runwaysFile);
        }
         catch (Exception e) {
             Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
             alert.setTitle("Error");
             alert.setHeaderText("File with runways not found.");
             alert.showAndWait();
             e.printStackTrace();
         }

    }

    private void openExistingAirport() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File saveDirectory = directoryChooser.showDialog(primaryStage);
        if (saveDirectory != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/MainView.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MainController mainController = fxmlLoader.getController();
                Airport airport = new Airport(saveDirectory);
                mainController.setAirport(airport);
                primaryStage.setScene(new Scene(root, 1000, 650));
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.setTitle("Error");
                alert.setHeaderText("Cannot load data.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
