package GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class GeneratorController {

    @FXML private TextField arrivedFlightsInput;
    @FXML private TextField waitingFlightsInput;
    @FXML private TextField departureFlightsInput;
    @FXML private Button generateBtn;
    @FXML private Button cancelBtn;
    private boolean confirmed;

    public void initialize() {
        confirmed = false;
        generateBtn.setOnAction(e -> {
            confirmed = true;
            closeForm();
        });
        cancelBtn.setOnAction(e -> {
            closeForm();
            confirmed = false;
        });
    }

    private void closeForm() {
        Stage stage = (Stage) generateBtn.getScene().getWindow();
        stage.close();
    }

    public Integer getArrivedFlights() {
        try {
            return Integer.parseInt(arrivedFlightsInput.getText());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input must be number.");
        }
    }

    public Integer getWaitingFlights() {
        try {
            return Integer.parseInt(waitingFlightsInput.getText());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input must be number.");
        }
    }

    public Integer getDepartureFlights() {
        try {
            return Integer.parseInt(departureFlightsInput.getText());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input must be number.");
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
