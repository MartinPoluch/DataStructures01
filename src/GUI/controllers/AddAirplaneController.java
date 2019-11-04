package GUI.controllers;

import Apk.Airplane;
import Apk.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAirplaneController {

    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField type;
    @FXML private TextField code;
    @FXML private TextField minLength;
    private boolean valid;

    public void initialize() {
        valid = false;
        confirmBtn.setOnAction(e -> {
            valid = true;
            closeForm();
        });
        cancelBtn.setOnAction(e -> closeForm());
    }

    private void closeForm() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }



    public Flight createAirplane() {
        Flight flight = null;
        if (valid) {
            try {
                int minLength = Integer.parseInt(this.minLength.getText());
                String typeStr = type.getText().replace(",", "");
                String codeStr = code.getText().replace(",", "");
                Airplane airplane = new Airplane(typeStr, codeStr, minLength);
                flight = new Flight(airplane);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Wrong input");
                alert.setContentText("Some of fields were empty or had wrong input type");

                alert.showAndWait();
            }

        }
        return flight;
    }
}
