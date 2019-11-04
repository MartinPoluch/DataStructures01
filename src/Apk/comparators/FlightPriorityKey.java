package Apk.comparators;

import Apk.Airplane;
import Apk.Flight;

import java.util.Objects;

public class FlightPriorityKey implements Comparable<FlightPriorityKey>{

    private Flight flight;

    public FlightPriorityKey(Flight flight) {
        this.flight = flight;
    }

    @Override
    public int compareTo(FlightPriorityKey other) {
        if (this.flight.getPriority().compareTo(other.flight.getPriority()) == 0) {
            return this.flight.getRunwayRequest().compareTo(other.flight.getRunwayRequest());
        }
        else {
            return this.flight.getPriority().compareTo(other.flight.getPriority());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightPriorityKey)) return false;
        FlightPriorityKey that = (FlightPriorityKey) o;
        return Objects.equals(this.flight.getPriority(), that.flight.getPriority());
    }
}
