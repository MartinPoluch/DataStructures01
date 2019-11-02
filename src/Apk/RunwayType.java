package Apk;

import Apk.comparators.FlightCodeKey;
import Apk.comparators.FlightPriorityKey;
import structures.HeapNode;
import structures.PairingHeap;
import structures.SplayTree;

import java.util.ArrayList;
import java.util.LinkedList;

public class RunwayType {

    private int length;
    private SplayTree<FlightCodeKey, Flight> waitingFlights; // lietadla ktore cakaju na pridelenie odletovej drahy
    private PairingHeap<FlightPriorityKey, Flight> waitingQueue; // lietadla ktore cakaju na pridelenie odletovej drahy
    private SplayTree<FlightCodeKey, Flight> flightsOnRunway; // vyuziva sa pri odlete, aby sme dokazali lietadlo ktore ma odletiet najist z lepsou zlozitostou
    private ArrayList<Runway> runways;
    private LinkedList<Integer> freeRunways; // zoznam volny drah (cisla reprezentuju indexy drah)
    private Airport airport;

    public RunwayType(int length, int quantity, Airport airport) {
        this.length = length;
        this.waitingFlights = new SplayTree<>();
        this.waitingQueue = new PairingHeap<>();
        this.flightsOnRunway = new SplayTree<>();
        this.runways = new ArrayList<>(quantity);
        this.freeRunways = new LinkedList<>();
        for (int id = 0; id < quantity; id++) {
            runways.add(new Runway(id, length));
            freeRunways.add(id);
        }
        this.airport = airport;
    }

    /**
     * Pouziva sa ako RunwayKey
     */
    public RunwayType(int length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void request(Flight flight) {
        if (flight == null) {
            return;
        }

        flight.setRunwayType(this);
        if (! freeRunways.isEmpty()) { // existuje volna draha
            addFlightOnRunway(flight);
        }
        else { // nie je ziadna volna draha, lietadlo sa zaradi medzi cakajuce lietadla
            waitingFlights.insert(new FlightCodeKey(flight), flight);
            HeapNode<FlightPriorityKey, Flight> flightNode = waitingQueue.insert(new FlightPriorityKey(flight), flight);
            flight.setHeapNode(flightNode);
            airport.addFlightToWaiting(flight);
            flight.getAirplane().setState(State.WAITING);
        }
    }

    /**
     * Priradi lietadlo konkretnu drahu.
     */
    private void addFlightOnRunway(Flight flight) {
        int runwayId = freeRunways.removeFirst();
        Runway freeRunway = runways.get(runwayId);
        freeRunway.occupy(flight);
        flight.setRunway(freeRunway);
        flightsOnRunway.insert(new FlightCodeKey(flight), flight);
        airport.addFlightToRunway(flight);
        flight.getAirplane().setState(State.ON_RUN_WAY);
    }

    public Flight departure(Flight departureFlight) {
        return removeFlightFromRunway(departureFlight, true);
    }

    public void changePriorityOfFlight(Flight flight, int newPriority) {
        int oldPriority = flight.getPriority();
        flight.setPriority(newPriority);
        FlightPriorityKey priorityKey = new FlightPriorityKey(flight);
        HeapNode<FlightPriorityKey, Flight> node = flight.getHeapNode();
        if (oldPriority < newPriority) { // stara priorita bola lepsia
            waitingQueue.decreasePriority(priorityKey, node);
        }
        else {
            waitingQueue.increasePriority(priorityKey, node);
        }
    }


    public Flight removeFlightFromRunway(Flight flight, boolean departure) {
        flightsOnRunway.remove(new FlightCodeKey(flight)); // flight je skutocne lietadlo zo vsetkymi vypisanymi atributmy
        Runway runway = flight.getRunway();
        if (departure) {
            runway.free(); // draha sa uvolni, lietadlo sa zaradi do historie
        }
        else {
            runway.remove(); // draha sa uvolni, lietadlo sa NEzaradi do historie
        }
        freeRunways.addLast(runway.getId()); // id novo uvolnenej drahy sa prida medzi idcka volnych drah;
        if (waitingQueue.getSize() > 0) {
            Flight nextFlight = waitingQueue.deleteMin();
            waitingFlights.remove(new FlightCodeKey(nextFlight));
            airport.removeFlightFromAllWaiting(nextFlight);
            addFlightOnRunway(nextFlight);
            return nextFlight;
        }
        else {
            return null;
        }
    }

    public void removeWaitingFlight(Flight flight) {
        waitingFlights.remove(new FlightCodeKey(flight));
        HeapNode<FlightPriorityKey, Flight> node = flight.getHeapNode();
        waitingQueue.deleteNode(node);
        flight.getAirplane().setState(State.INACTIVE);
    }

    public SplayTree<FlightCodeKey, Flight> getWaitingFlights() {
        return waitingFlights;
    }

    public ArrayList<Runway> getRunways() {
        return runways;
    }

    @Override
    public String toString() {
        return Integer.toString(length);
    }
}
