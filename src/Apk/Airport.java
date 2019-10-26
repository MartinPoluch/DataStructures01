package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.RunwayKey;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import structures.SplayTree;
import structures.TreeNode;

import java.io.File;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Airport {

    private LocalDateTime actualDateTime;
    private SplayTree<RunwayKey, RunwayType> runways;
    private SplayTree<AirplaneCodeKey, Airplane> allAirplanes;
    private SplayTree<FlightCodeKey, Flight> arrivedFlights; // lietadla ktore nemaju definovany cas poziadania o odletovu drahu (este neprileteli)
    private SplayTree<FlightCodeKey, Flight> waitingFlights; // lietadla ktore cakaju na pridelenie odletovej drahy
    private SplayTree<FlightCodeKey, Flight> flightsOnRunway;

    public Airport(LocalDateTime datetime) {
        this.actualDateTime = datetime;
        this.allAirplanes = new SplayTree<>();
        this.arrivedFlights = new SplayTree<>();
        this.waitingFlights = new SplayTree<>();
        this.runways = new SplayTree<>();
        this.flightsOnRunway = new SplayTree<>();
        this.readNumbersOfRunways();
    }

    private void readNumbersOfRunways() {
        //TODO, kedze v subore su data ulozene vzostupne, splay strom bude na zaciatku zdegenerovany, riesenie ???
        File numberOfRunways = new File("src\\Apk\\runways.txt");
        try {
            Scanner scanner = new Scanner(numberOfRunways);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parseLine = line.split(" : ");
                int length = Integer.parseInt(parseLine[0]);
                int quantity = Integer.parseInt(parseLine[1]);
                RunwayType runwayType = new RunwayType(length, quantity, this);
                runways.insert(new RunwayKey(runwayType), runwayType);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addFlight(Flight flight) {
        if (flight != null) {
            flight.setArrival(actualDateTime);
            Airplane temporaryAirplane = flight.getAirplane();
            Airplane airplane = allAirplanes.findOrInsert(new AirplaneCodeKey(temporaryAirplane), temporaryAirplane); // ak uz lietadlo existuje v systeme tak si vytiaheme referenciu
            airplane.setState(State.ARRIVED);
            flight.setAirplane(airplane); // priradime k letu novu refeneciu (v pripade ze lietadlo uz existuje v systeme)
            arrivedFlights.insert(new FlightCodeKey(flight), flight);
        }
        printAll();
    }

    public void requestRunway(String code) throws IllegalArgumentException, NoSuchElementException {
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = arrivedFlights.remove(key);
        if (flight != null) {
            int airplaneLength = flight.getAirplane().getMinRunwayLength();
            RunwayType runwayType = runways.findFirstBiggerValue(new RunwayKey(new RunwayType(airplaneLength)));
            if (runwayType != null) {
                runwayType.request(flight);
            }
            else {
                allAirplanes.remove(new AirplaneCodeKey(flight.getAirplane()));
                throw new NoSuchElementException("There is no runway which satisfies minimal runway length for this airplane. Airplane was removed from the system");
            }
        }
        else {
            throw new IllegalArgumentException("There is no arrived flight with this code.");
        }
        printAll();
    }

    public void addFlightToWaiting(Flight flight) {
        waitingFlights.insert(new FlightCodeKey(flight), flight);
    }

    public void removeFlightFromWaiting(Flight flight) {
        waitingFlights.remove(new FlightCodeKey(flight));
    }

    public void addFlightToRunway(Flight flight) {
        flightsOnRunway.insert(new FlightCodeKey(flight), flight);
    }

    public void removeFlightFromRunway(Flight flight) {
        flightsOnRunway.remove(new FlightCodeKey(flight));
    }

    public String getActualDateTimeValue() {
        return actualDateTime.toLocalDate()  + " " + actualDateTime.toLocalTime();
    }

    private void printAll() {
        System.out.println("\n--------------------------------------------------------------------------\n");
        printSplayTree("All planes", allAirplanes);
        printSplayTree("Arrived flights", arrivedFlights);
        printSplayTree("Waiting flight", waitingFlights);
        printSplayTree("Flights on runway ", flightsOnRunway);
    }

    private <K extends Comparable<K>, V> void printSplayTree(String name, SplayTree<K, V> splayTree) {
        System.out.println(name);
        for (TreeNode<K, V> node : splayTree.inOrder()) {
            System.out.println(node.getValue());
        }
        System.out.println("\n");
    }
}
