package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/view.fxml"));
        primaryStage.setTitle("Hello World");
        VBox vBox = new VBox();
        DateTimePicker d = new DateTimePicker();
        vBox.getChildren().add(d);
        Button button = new Button();
        button.setOnAction(e -> printDateTime(d));
        vBox.getChildren().add(button);
        primaryStage.setScene(new Scene(vBox, 300, 275));
        primaryStage.show();

    }

    private static void printDateTime(DateTimePicker picker){
        System.out.println(picker.getDateTimeValue());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
