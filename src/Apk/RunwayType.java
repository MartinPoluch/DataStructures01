package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.FlightPriorityKey;
import structures.PairingHeap;
import structures.SplayTree;

import java.util.ArrayList;
import java.util.LinkedList;

public class RunwayType {

    private int length;
    private SplayTree<FlightCodeKey, Flight> waitingFlights; // lietadla ktore cakaju na pridelenie odletovej drahy
    private PairingHeap<FlightPriorityKey, Flight> waitingQueue; // lietadla ktore cakaju na pridelenie odletovej drahy
    private SplayTree<FlightCodeKey, Flight> flightsOnRunway;
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
            runways.add(new Runway(id));
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
        Airplane airplane = flight.getAirplane();
        if (! freeRunways.isEmpty()) { // vsetky drahy su obsadene
            int runwayId = freeRunways.removeFirst();
            Runway freeRunway = runways.get(runwayId);
            flight.setRunway(freeRunway);
            flightsOnRunway.insert(new FlightCodeKey(flight), flight);
            airport.addFlightToRunway(flight);
            airplane.setState(State.ON_RUN_WAY);
        }
        else { // nie je ziadna volna draha, lietadlo sa zaradi medzi cakajuce lietadla
            waitingFlights.insert(new FlightCodeKey(flight), flight);
            waitingQueue.insert(new FlightPriorityKey(flight), flight);
            airport.addFlightToWaiting(flight);
            airplane.setState(State.WAITING);
        }
    }

    public SplayTree<FlightCodeKey, Flight> getWaitingFlights() {
        return waitingFlights;
    }
}
