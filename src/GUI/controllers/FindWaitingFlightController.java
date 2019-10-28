package GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FindWaitingFlightController {

    @FXML private TextField codeInput;
    @FXML private TextField runwayInput;
    @FXML private Button findBtn;
    @FXML private Button cancelBtn;
    @FXML private boolean confirmed;

    public void initialize() {

        findBtn.setOnAction(e -> {
            confirmed = true;
            closeForm();
        });
        cancelBtn.setOnAction(e -> {
            confirmed = false;
            closeForm();
        });
    }

    private void closeForm() {
        Stage stage = (Stage) findBtn.getScene().getWindow();
        stage.close();
    }

    public String getCode() {
        return codeInput.getText();
    }

    public Integer getRunwayType() {
        try {
            return Integer.parseInt(runwayInput.getText()); // ak je to cislo tak vrati int
        }
        catch (Exception e) {
            return null;// inak vrati null
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
