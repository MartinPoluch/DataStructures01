package GUI;

import Apk.Airport;
import Apk.Flight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class MainController {


    @FXML private Button addAirplaneBtn;
    @FXML private Button addRunwayRequestBtn;
    @FXML private Label dateTime;
    private Airport airport;


    public void initialize() {
        addAirplaneBtn.setOnAction(e -> openAddAirplaneForm());
        addRunwayRequestBtn.setOnAction(e -> openRunwayRequestForm());
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
        dateTime.setText(airport.getActualDateTimeValue());
    }

    private void openAddAirplaneForm() {
        Stage addAirPlaneForm = new Stage();
        addAirPlaneForm.setTitle("Add new airplane");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/AddAirplaneView.fxml"));
            Parent root = fxmlLoader.load();
            addAirPlaneForm.setScene(new Scene(root));
            AddAirplaneController controller = fxmlLoader.getController();
            addAirPlaneForm.initModality(Modality.APPLICATION_MODAL);
            addAirPlaneForm.showAndWait();
            Flight flight = controller.createAirplane();
            if (flight != null) {
                airport.addFlight(flight);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRunwayRequestForm() {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Request runway");
        dialog.setHeaderText("Insert request for runway.");
        dialog.setContentText("Enter airplane unique code:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(code -> {
            try {
                airport.requestRunway(code);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Request cannot be done");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }
}
