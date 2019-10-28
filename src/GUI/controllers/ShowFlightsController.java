package GUI.controllers;

import Apk.Flight;
import Apk.comparators.FlightCodeKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import structures.SplayTree;

import java.time.LocalDateTime;

public class ShowFlightsController {

    @FXML private TableColumn<Flight, String> codeCol;
    @FXML private TableColumn<Flight, String> typeCol;
    @FXML private TableColumn<Flight, Integer> minLengthCol;
    @FXML private TableColumn<Flight, LocalDateTime> arrivalCol;
    @FXML private TableColumn<Flight, LocalDateTime> runwayRequestCol;
    @FXML private TableColumn<Flight, LocalDateTime> departureCol;
    @FXML private TableColumn<Flight, Integer> priorityCol;

    @FXML private TableView<Flight> flightsTab;
    @FXML private ObservableList<Flight> flights;

    public void initialize()  {
        flights = FXCollections.observableArrayList();
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        minLengthCol.setCellValueFactory(new PropertyValueFactory<>("minLength"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        runwayRequestCol.setCellValueFactory(new PropertyValueFactory<>("runwayRequest"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departure"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
    }

    public void showFlights(SplayTree<FlightCodeKey, Flight> tree) {
        tree.inOrder(flights);
        flightsTab.setItems(flights);
    }

    public void showFlights(Flight flight) {
        if (flight != null) {
            flights.add(flight);
        }
        flightsTab.setItems(flights);
    }

}
