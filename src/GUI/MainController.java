package GUI;

import Apk.Airplane;
import Apk.Airport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button addAirplaneBtn;
    private Airport airport;

    public void initialize() {
        airport = new Airport();
        addAirplaneBtn.setOnAction(e -> openAddAirplaneForm());
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
            Airplane airplane = controller.createAirplane();
            if (airplane != null) {
                airport.addAirplane(airplane);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
