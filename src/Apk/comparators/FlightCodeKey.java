package Apk.comparators;

import Apk.Flight;

import java.util.Objects;

public class FlightCodeKey implements Comparable<FlightCodeKey> {

    private Flight flight;

    public FlightCodeKey(Flight flight) {
        this.flight = flight;
    }

    @Override
    public int compareTo(FlightCodeKey other) {
        return this.flight.getAirplane().getCode().compareTo(other.flight.getAirplane().getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightCodeKey)) return false;
        FlightCodeKey that = (FlightCodeKey) o;
        return Objects.equals( this.flight.getCode(), that.flight.getCode());
    }

}
