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
    private SplayTree<FlightCodeKey, Flight> flightsOnRunway; //TODO potrebujem vobec tento atribut ???
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
        if (! freeRunways.isEmpty()) { // existuje volna draha
            addFlightOnRunway(flight);
        }
        else { // nie je ziadna volna draha, lietadlo sa zaradi medzi cakajuce lietadla
            waitingFlights.insert(new FlightCodeKey(flight), flight);
            waitingQueue.insert(new FlightPriorityKey(flight), flight);
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

    public void departure(Flight departureFlight) {
        flightsOnRunway.remove(new FlightCodeKey(departureFlight)); // flight je skutocne lietadlo zo vsetkymi vypisanymi atributmy
        Runway runway = departureFlight.getRunway();
        runway.free(); // draha sa uvolni
        freeRunways.addLast(runway.getId()); // id novo uvolnenej drahy sa prida medzi idcka volnych drah;
        System.out.println("departure: " + departureFlight);
        if (waitingQueue.getSize() > 0) {
            Flight nextFlight = waitingQueue.deleteMin();
            waitingFlights.remove(new FlightCodeKey(nextFlight));
            airport.removeFlightFromWaiting(nextFlight);
            addFlightOnRunway(nextFlight);
            System.out.println("next flight: " + nextFlight);
        }

    }

    public SplayTree<FlightCodeKey, Flight> getWaitingFlights() {
        return waitingFlights;
    }

    @Override
    public String toString() {
        return Integer.toString(length);
    }
}
