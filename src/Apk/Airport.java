package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.RunwayTypeKey;
import structures.SplayTree;
import structures.TreeNode;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Airport {

    private LocalDateTime actualDateTime;
    private SplayTree<RunwayTypeKey, RunwayType> runways;
    private SplayTree<AirplaneCodeKey, Airplane> allAirplanes;
    private SplayTree<FlightCodeKey, Flight> arrivedFlights; // lietadla ktore nemaju definovany cas poziadania o odletovu drahu (este neprileteli)
    private SplayTree<FlightCodeKey, Flight> allWaitingFlights; // lietadla ktore cakaju na pridelenie odletovej drahy
    private SplayTree<FlightCodeKey, Flight> allFlightsOnRunway;

    private DataGenerator dataGenerator;

    public Airport(LocalDateTime datetime, File runwaysFile) {
        this.actualDateTime = datetime;
        this.allAirplanes = new SplayTree<>();
        this.arrivedFlights = new SplayTree<>();
        this.allWaitingFlights = new SplayTree<>();
        this.runways = new SplayTree<>();
        this.allFlightsOnRunway = new SplayTree<>();
        this.dataGenerator = new DataGenerator();
        this.readNumbersOfRunways(runwaysFile);
    }

    public Airport(File saveDirectory) throws IOException, IllegalArgumentException{
        FileLoader fileLoader = new FileLoader(saveDirectory);
        this.actualDateTime = fileLoader.loadDateTime();
        this.allAirplanes = fileLoader.loadAllAirplanes();
        this.arrivedFlights = fileLoader.loadArrivedFlights();
        this.allWaitingFlights = new SplayTree<>();
        this.allFlightsOnRunway = new SplayTree<>();
        this.runways = fileLoader.loadRunways(this);
        //printAll();
    }

    public void saveDataToFiles(File locationOfDirectory) throws IllegalArgumentException, IOException {
        FileSaver fileSaver = new FileSaver(locationOfDirectory);
        fileSaver.save(actualDateTime);
        fileSaver.save(allAirplanes);
        fileSaver.save(arrivedFlights, FileSaver.ARRIVED_FLIGHTS);
        List<RunwayType> runwayTypes = runways.levelOrder();
        for (RunwayType runwayType : runwayTypes) {
            fileSaver.save(runwayType);
        }
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

    private void readNumbersOfRunways(File runwaysFile) {
        // data v subore su usporiadane podla dlzky drahy, pri nacitani drah priamo do splay stromu by bol splay strom zdegenerovany
        // z toho dovodu drahy najskor nacitam do arrayListu, kde nasledne nahodne zmenim poradie prvkov (shuffle)
        List<RunwayType> antiDegeneration = new ArrayList<>();
        int biggestRunway = 0; //TODO mohol by to byt atribut a pri pridavani lietadla sa bude checkovat dlzka
        try {
            Scanner scanner = new Scanner(runwaysFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parseLine = line.split(",");
                int length = Integer.parseInt(parseLine[0]);
                if (length > biggestRunway) {
                    biggestRunway = length;
                }
                int quantity = Integer.parseInt(parseLine[1]);
                RunwayType runwayType = new RunwayType(length, quantity, this);
                antiDegeneration.add(runwayType);
            }
            dataGenerator.setMaxRunwayLength(biggestRunway);

            Collections.shuffle(antiDegeneration);
            for (RunwayType runwayType : antiDegeneration) {
                runways.insert(new RunwayTypeKey(runwayType), runwayType);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Zlozitost: O(log N) + O(log A)
     * @param flight
     * @throws IllegalArgumentException
     */
    public void addFlight(Flight flight) throws IllegalArgumentException{
        if (flight != null) {
            flight.setArrival(actualDateTime);
            Airplane temporaryAirplane = flight.getAirplane();
            //O(log N)
            Airplane airplane = allAirplanes.findOrInsert(new AirplaneCodeKey(temporaryAirplane), temporaryAirplane); // ak uz lietadlo existuje v systeme tak si vytiaheme referenciu
            if (airplane.getState() != State.INACTIVE) {
                throw new IllegalArgumentException("Flight with code: " + flight.getCode() + " , is already on the airport. " +
                        "State of the flight: " + airplane.getState());
            }
            airplane.setState(State.ARRIVED);
            flight.setAirplane(airplane); // priradime k letu novu refeneciu (v pripade ze lietadlo uz existuje v systeme)
            arrivedFlights.insert(new FlightCodeKey(flight), flight); //O(log A)
        }
    }

    /**
     * Zlozitost: O(log A) + O(log T)
     * @param code
     * @param priority
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    public void requestRunway(String code, int priority) throws IllegalArgumentException, NoSuchElementException {
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = arrivedFlights.remove(key); // lietadlo musi byt priletene aby mohlo poziadat o drahu
        if (flight != null) {
            int airplaneLength = flight.getAirplane().getMinRunwayLength();
            RunwayType runwayType = runways.findFirstBiggerValue(new RunwayTypeKey(new RunwayType(airplaneLength)));
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
    }

    public Flight addFlightDeparture(String code) throws IllegalArgumentException {
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = allFlightsOnRunway.remove(key);
        if (flight != null) {
            flight.setDeparture(actualDateTime);
            flight.getAirplane().setState(State.INACTIVE);// ON_RUN_WAY -> INACTIVE
            RunwayType runwayType = flight.getRunwayType();
            return runwayType.departure(flight);
        }
        else {
            throw new IllegalArgumentException("Flight with code " + code + " is not on runway");
        }
    }

    public Flight findFlightOnRunway(FlightCodeKey flightKey, RunwayTypeKey runwayKey) {
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

    public void removeFlightFromAllWaiting(Flight flight) {
        allWaitingFlights.remove(new FlightCodeKey(flight));
    }

    /**
     * Lietadla sa budu do zoznamu vsetkych lietadiel na drahe pridavat len z triedy runWayType
     */
    public void
    addFlightToRunway(Flight flight) {
        allFlightsOnRunway.insert(new FlightCodeKey(flight), flight);
    }

    public void removeFlightFromRunway(Flight flight) {
        allFlightsOnRunway.remove(new FlightCodeKey(flight));
    }

    public String getActualDateTimeValue() {
        return actualDateTime.toLocalDate() + " " + actualDateTime.toLocalTime();
    }

    public SplayTree<FlightCodeKey, Flight> findWaitingFlightsForRunway(int runwayLength) throws IllegalArgumentException{
        RunwayTypeKey runwayKey = new RunwayTypeKey(new RunwayType(runwayLength));
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
        System.out.println("\n--------------------------------------------------------------------------\n");
        printSplayTree("All planes", allAirplanes);
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

    public void generateData(int arrivedFlights, int waitingFlights, int departureFlights) {
        for (int i = 0; i < departureFlights; i++) { // najskor treba generovat odlety, pretoze potom uz budu obsadene drahy
            Flight flight = dataGenerator.randomFlight();
            addFlight(flight);
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
            requestRunway(flight.getCode(), dataGenerator.randomNumber(10) + 1);
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
            addFlightDeparture(flight.getCode());
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
        }

        for (int i = 0; i < arrivedFlights; i++) {
            Flight flight = dataGenerator.randomFlight();
            addFlight(flight);
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
        }

        for (int i = 0; i < waitingFlights; i++) {
            Flight flight = dataGenerator.randomFlight();
            addFlight(flight);
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
            int priority = dataGenerator.randomNumber(10) + 1;
            requestRunway(flight.getCode(), priority);
            actualDateTime = dataGenerator.updateDatetime(actualDateTime);
        }
    }

    public List<RunwayType> getRunwayTypes() {
        List<RunwayType> types = new ArrayList<>();
        runways.inOrder(types);
        return types;
    }

    public SplayTree<FlightCodeKey, Flight> getAllFlightsOnRunway() {
        return allFlightsOnRunway;
    }

    public SplayTree<FlightCodeKey, Flight> getArrivedFlights() {
        return arrivedFlights;
    }

    public void cancelFlight(String code) throws IllegalArgumentException {
        FlightCodeKey flightCodeKey = new FlightCodeKey(new Flight(code));
        Flight flight = allWaitingFlights.remove(flightCodeKey); // skusim vymazt lietadlo z cakajucich lietadiel
        if (flight != null) {
            flight.getAirplane().setState(State.INACTIVE);
            RunwayType runwayType = flight.getRunwayType();
            runwayType.removeWaitingFlight(flight);
            return; // aby lietadlo nebolo dalej vyhladavane v lietadlach na drahe
        }

        flight = allFlightsOnRunway.remove(flightCodeKey);
        if (flight != null) {
            flight.getAirplane().setState(State.INACTIVE);
            RunwayType runwayType = flight.getRunwayType();
            runwayType.removeFlightFromRunway(flight, false);
        }
        else {
            throw new IllegalArgumentException("Flight with code: " + code + " is not in waiting flights.");
        }
    }

    public void changePriority(String code, int newPriority) {
        FlightCodeKey key = new FlightCodeKey(new Flight(code));
        Flight flight = allWaitingFlights.find(key);
        if (flight != null) {
            if (flight.getPriority() != newPriority) { // ak je nova priorita rovnaka ako stara tak nema zmysel ju menit
                RunwayType runwayType = flight.getRunwayType();
                runwayType.changePriorityOfFlight(flight, newPriority);
            }
        }
        else {
            throw new IllegalArgumentException("Flight with code: " + code + " is not in waiting flights.");
        }
    }

}
