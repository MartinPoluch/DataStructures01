package Apk.comparators;

import Apk.Airplane;
import Apk.Flight;

public class FlightPriorityKey implements Comparable<FlightPriorityKey>{

    private Flight flight;

    public FlightPriorityKey(Flight flight) {
        this.flight = flight;
    }

    @Override
    public int compareTo(FlightPriorityKey other) {
        //TODO treba zohladnit aj cas, pri rovnakej priorite ma prednost lietadlo ktore caka dlhsie (skorej poziadalo o odletovu drahu)
        return this.flight.getPriority().compareTo(other.flight.getPriority());
    }
}
