package GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RequestRunwayController {

    @FXML private TextField code;
    @FXML private TextField priority;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    private boolean confirmed;

    public void initialize() {
        confirmed = false;
        confirmBtn.setOnAction(e -> {
            closeForm();
            confirmed = true;
        });
        cancelBtn.setOnAction(e -> {
            closeForm();
            confirmed = false;
        });
    }

    public void closeForm() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCode() {
        return code.getText();
    }

    public Integer getPriority() {
        try {
            return Integer.parseInt(priority.getText());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Priority must be number.");
        }
    }
}
