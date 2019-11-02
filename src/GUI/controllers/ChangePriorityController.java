package GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangePriorityController {

    @FXML private TextField codeInput;
    @FXML private TextField priorityInput;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private boolean confirmed;

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

    public Integer getPriority() {
        try {
            return Integer.parseInt(priorityInput.getText());
        } catch (Exception e) {
            throw new IllegalArgumentException("Priority must be number.");
        }
    }

    public String getCode() {
        return codeInput.getText();
    }
}
