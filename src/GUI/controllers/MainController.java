package GUI.controllers;

import Apk.Airport;
import Apk.Flight;
import Apk.RunwayType;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.RunwayKey;
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
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import structures.SplayTree;

import java.util.Optional;

public class MainController {


    @FXML private Button addAirplaneBtn;
    @FXML private Button addRunwayRequestBtn;
    @FXML private Button findWaitingFlightBtn;
    @FXML private Button showWaitingFlightsBtn;
    @FXML private Button showWFForRunwayBtn; // show waiting flights for runway
    @FXML private Label dateTime;
    @FXML private Button addMinutesBtn;
    @FXML private Button addHourBtn;
    @FXML private Button addDayBtn;

    @FXML private Button generateDataBtn;

    private Airport airport;

    public void initialize() {
        addAirplaneBtn.setOnAction(e -> openAddAirplaneForm());
        addRunwayRequestBtn.setOnAction(e -> openRunwayRequestForm());
        showWaitingFlightsBtn.setOnAction(e -> showFlights(airport.getWaitingFlights()));
        findWaitingFlightBtn.setOnAction(e -> openFindWaitingFLightForm());
        showWFForRunwayBtn.setOnAction(e -> openShowWForRunwayForm());
        generateDataBtn.setOnAction(e -> openGenerateDataForm());
        addMinutesBtn.setOnAction(e -> {
            airport.addTime(5, "minutes");
            dateTime.setText(airport.getActualDateTimeValue());
        });
        addHourBtn.setOnAction(e -> {
            airport.addTime(1, "hour");
            dateTime.setText(airport.getActualDateTimeValue());
        });
        addDayBtn.setOnAction(e -> {
            airport.addTime(1, "day");
            dateTime.setText(airport.getActualDateTimeValue());
        });
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
        dateTime.setText(airport.getActualDateTimeValue());
    }

    private void openAddAirplaneForm() {
        Stage addAirPlaneForm = new Stage();
        addAirPlaneForm.setTitle("Add new airplane");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/AddAirplaneView.fxml"));
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

    private void openFindWaitingFLightForm() {
        Stage stage = new Stage();
        stage.setTitle("Find flight");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/FindWaitingFlightView.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            FindWaitingFlightController controller = fxmlLoader.getController();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                String code = controller.getCode();
                FlightCodeKey flightKey = new FlightCodeKey(new Flight(code));
                Integer runwayLength = controller.getRunwayType();
                Flight foundFlight = null;
                if (runwayLength == null) {
                    SplayTree<FlightCodeKey, Flight> allWaitingFlight = airport.getWaitingFlights();
                    foundFlight = allWaitingFlight.find(flightKey);
                }
                else {
                    RunwayKey runwayKey = new RunwayKey(new RunwayType(runwayLength));
                    foundFlight = airport.findFlightOnRunway(flightKey, runwayKey);
                }
                if (foundFlight != null) {
                    showFlights(foundFlight);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Flight not found");
                    alert.showAndWait();
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRunwayRequestForm() {
        try {
            Stage form = new Stage();
            form.setTitle("Request runway");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/RequestRunwayView.fxml"));
            System.out.println(fxmlLoader);
            Parent root = (Parent) fxmlLoader.load();
            System.out.println(root);
            form.setScene(new Scene(root));
            RequestRunwayController controller = fxmlLoader.getController();
            System.out.println(controller);
            form.showAndWait();
            if (controller.isConfirmed()) {
                String code = controller.getCode();
                Integer priority = controller.getPriority();
                airport.requestRunway(code, priority);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Request cannot be done");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            e.printStackTrace();
        }
//        TextInputDialog dialog = new TextInputDialog("");
//
//        dialog.setTitle("Request runway");
//        dialog.setHeaderText("Insert request for runway.");
//        dialog.setContentText("Enter airplane unique code:");
//
//        Optional<String> result = dialog.showAndWait();
//
//        result.ifPresent(code -> {
//            try {
//                airport.requestRunway(code);
//            }
//            catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//                alert.setHeaderText("Request cannot be done");
//                alert.setContentText(e.getMessage());
//                alert.showAndWait();
//                e.printStackTrace();
//            }
//        });
    }

    private void openShowWForRunwayForm() {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Find flights waiting for runway");
        dialog.setHeaderText("Find flights waiting for runway.");
        dialog.setContentText("Enter length of runway:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            try {
                int length = Integer.parseInt(input);
                SplayTree<FlightCodeKey, Flight> waitingFlights = airport.findWaitingFlightsForRunway(length);
                showFlights(waitingFlights);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.setTitle("Error");
                alert.setHeaderText("Runway not found");
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }

    private void openGenerateDataForm() {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Generate data");
        dialog.setHeaderText("Generate number of waiting flights.");
        dialog.setContentText("Enter number of waiting flights:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            try {
                int numOfWaitingFlights = Integer.parseInt(input);
                airport.generateData(numOfWaitingFlights);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.setTitle("Error");
                alert.setHeaderText("Wrong input");
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }

    private void showFlights(SplayTree<FlightCodeKey, Flight> flights) {
        try {
            Stage tab = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/ShowFlightsView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            tab.setScene(new Scene(root));
            ShowFlightsController controller = fxmlLoader.getController();
            controller.showFlights(flights);
            tab.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFlights(Flight flight) {
        if (flight == null) {
            return;
        }
        try {
            Stage tab = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/views/ShowFlightsView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            tab.setScene(new Scene(root));
            ShowFlightsController controller = fxmlLoader.getController();
            controller.showFlights(flight);
            tab.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
