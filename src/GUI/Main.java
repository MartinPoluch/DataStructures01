package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage paPrimaryStage) throws Exception{
        primaryStage = paPrimaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/MainView.fxml"));
        primaryStage.setTitle("Airport");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
