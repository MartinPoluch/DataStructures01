package Apk;

import java.util.LinkedList;

public class Runway {

    private final int id;
    private final int length;
    private Flight flight;
    private LinkedList<Flight> flightsHistory;

    public Runway(int id, int length) {
        this.id = id;
        this.length = length;
        this.flight = null;
        this.flightsHistory = new LinkedList<>();
    }

    public Runway(int id, int length,LinkedList<Flight> flightsHistory) {
        this.id = id;
        this.length = length;
        this.flight = null;
        this.flightsHistory = flightsHistory;
    }

    public int getId() {
        return id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void occupy(Flight flight) {
        this.flight = flight;
    }

    public void free() {
        flightsHistory.addLast(flight);
        flight = null;
    }

    public void remove() {
        flight = null;
    }

    public LinkedList<Flight> getFlightsHistory() {
        return flightsHistory;
    }

    @Override
    public String toString() {
        return Integer.toString(length) + "-" + Integer.toString(id);
    }
}
