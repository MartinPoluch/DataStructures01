package GUI;

import Apk.Airplane;
import javafx.fxml.FXML;
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
    private DateTimePicker datePicker;
    private boolean valid;



    public void initialize() {
        datePicker = new DateTimePicker();
        dateTimeBox.getChildren().add(datePicker);
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



    public Airplane createAirplane() {
        //TODO mozno pridat validaciu dat
        Airplane airplane = null;
        if (valid) {
            int minLength = Integer.parseInt(this.minLength.getText());
            int priority = Integer.parseInt(this.priority.getText());
            airplane = new Airplane(type.getText(), code.getText(), minLength, datePicker.getDateTimeValue(), priority);
        }
        return airplane;
    }
}
