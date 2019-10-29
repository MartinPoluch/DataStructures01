package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.RunwayKey;
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
    private SplayTree<FlightCodeKey, Flight> allWaitingFlights; // lietadla ktore cakaju na pridelenie odletovej drahy
    private SplayTree<FlightCodeKey, Flight> allFlightsOnRunway;

    private DataGenerator dataGenerator;


    public Airport(LocalDateTime datetime) {
        this.actualDateTime = datetime;
        this.allAirplanes = new SplayTree<>();
        this.arrivedFlights = new SplayTree<>();
        this.allWaitingFlights = new SplayTree<>();
        this.runways = new SplayTree<>();
        this.allFlightsOnRunway = new SplayTree<>();
        this.dataGenerator = new DataGenerator();
        this.readNumbersOfRunways();
    }

    public void addTime(int amount, String unit) {
        switch (unit) {
            case "minutes": {
                actualDateTime = actualDateTime.plusMinutes(amount);
                break;
            }
            case "hour": {
                actualDateTime = actualDateTime.plusHours(amount);
                break;
            }
            case "day": {
                actualDateTime = actualDateTime.plusDays(amount);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void readNumbersOfRunways() {
        //TODO, kedze v subore su data ulozene vzostupne, splay strom bude na zaciatku zdegenerovany, riesenie ???
        int biggestRunway = 0; //TODO mohol by to byt atribut a pri pridavani lietadla sa bude checkovat dlzka
        File numberOfRunways = new File("src\\Apk\\runways.txt");
        try {
            Scanner scanner = new Scanner(numberOfRunways);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parseLine = line.split(" : ");
                int length = Integer.parseInt(parseLine[0]);
                if (length > biggestRunway) {
                    biggestRunway = length;
                }
                int quantity = Integer.parseInt(parseLine[1]);
                RunwayType runwayType = new RunwayType(length, quantity, this);
                runways.insert(new RunwayKey(runwayType), runwayType);
            }
            dataGenerator.setMaxRunwayLength(biggestRunway);
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

    public void requestRunway(String code, int priority) throws IllegalArgumentException, NoSuchElementException {
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = arrivedFlights.remove(key); // lietadlo musi byt priletene aby mohlo poziadat o drahu
        if (flight != null) {
            int airplaneLength = flight.getAirplane().getMinRunwayLength();
            RunwayType runwayType = runways.findFirstBiggerValue(new RunwayKey(new RunwayType(airplaneLength)));
            if (runwayType != null) {
                flight.setPriority(priority);
                flight.setRunwayRequest(actualDateTime);
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

    public void addFlightDeparture(String code) throws IllegalArgumentException{
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = allFlightsOnRunway.remove(key);
        if (flight != null) {
            flight.setDeparture(actualDateTime);
            flight.departure();
        }
        else {
            throw new IllegalArgumentException("Flight with code " + code + " is not on runway");
        }
    }

    public Flight findFlightOnRunway(FlightCodeKey flightKey, RunwayKey runwayKey) {
        RunwayType runwayType = runways.find(runwayKey);
        if (runwayType != null) {
            return runwayType.getWaitingFlights().find(flightKey);
        }
        else {
            return null;
        }
    }


    /**
     * Lietadla sa budu do zoznamu vsetkych cakajucich lietadiel pridavat len z triedy runWayType
     */
    public void addFlightToWaiting(Flight flight) {
        allWaitingFlights.insert(new FlightCodeKey(flight), flight);
    }

    public void removeFlightFromWaiting(Flight flight) {
        allWaitingFlights.remove(new FlightCodeKey(flight));
    }

    /**
     * Lietadla sa budu do zoznamu vsetkych lietadiel na drahe pridavat len z triedy runWayType
     */
    public void addFlightToRunway(Flight flight) {
        allFlightsOnRunway.insert(new FlightCodeKey(flight), flight);
    }

    public void removeFlightFromRunway(Flight flight) {
        allFlightsOnRunway.remove(new FlightCodeKey(flight));
    }

    public String getActualDateTimeValue() {
        return actualDateTime.toLocalDate() + " " + actualDateTime.toLocalTime();
    }

    public SplayTree<FlightCodeKey, Flight> findWaitingFlightsForRunway(int runwayLength) throws IllegalArgumentException{
        RunwayKey runwayKey = new RunwayKey(new RunwayType(runwayLength));
        RunwayType runwayType = runways.find(runwayKey);
        if (runwayType != null) {
            return runwayType.getWaitingFlights();
        }
        else {
            throw new IllegalArgumentException("There is no runway of this length.");
        }
    }

    public SplayTree<FlightCodeKey, Flight> getAllWaitingFlights() {
        return allWaitingFlights;
    }

    private void printAll() {
//        System.out.println("\n--------------------------------------------------------------------------\n");
//        printSplayTree("All planes", allAirplanes);
//        printSplayTree("Arrived flights", arrivedFlights);
//        printSplayTree("Waiting flight", allWaitingFlights);
//        printSplayTree("Flights on runway ", allFlightsOnRunway);
    }

    private <K extends Comparable<K>, V> void printSplayTree(String name, SplayTree<K, V> splayTree) {
        System.out.println(name);
        for (TreeNode<K, V> node : splayTree.inOrder()) {
            System.out.println(node.getValue());
        }
        System.out.println("\n");
    }

    public void generateData(int waitingFlights) {
        for (int i = 0; i < waitingFlights; i++) {
            Flight flight = dataGenerator.randomFlight();
            addFlight(flight);
            requestRunway(flight.getCode(), flight.getPriority());
        }
    }


}
