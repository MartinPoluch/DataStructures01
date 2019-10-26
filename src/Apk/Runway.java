package Apk;

import java.util.LinkedList;

public class Runway {

    private int id;
    private Flight flight;
    private LinkedList<Flight> flightsHistory;

    public Runway(int id) {
        this.id = id;
        this.flight = null;
        this.flightsHistory = new LinkedList<>();
    }

    public void occupy(Flight flight) {
        this.flight = flight;
    }

    public Flight free() {
        flightsHistory.addLast(flight);
        flight = null;
        return flightsHistory.getLast();
    }
}
