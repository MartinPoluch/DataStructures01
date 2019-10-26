package GUI;

import Apk.Airplane;
import Apk.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AddAirplaneController {

    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private TextField type;
    @FXML private TextField code;
    @FXML private TextField minLength;
    @FXML private TextField priority;
    @FXML private HBox dateTimeBox;
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
        //TODO mozno pridat validaciu dat
        Flight flight = null;
        if (valid) {
            try {
                int minLength = Integer.parseInt(this.minLength.getText());
                int priority = Integer.parseInt(this.priority.getText());
                Airplane airplane = new Airplane(type.getText(), code.getText(), minLength);
                flight = new Flight(airplane, priority);
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
