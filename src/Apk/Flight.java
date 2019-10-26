package Apk;

import java.time.LocalDateTime;

public class Flight {

    private LocalDateTime arrival; // cas a datum priletu
    private LocalDateTime runwayRequest; // cas a datum poziadania o priletovu drahu
    private LocalDateTime departure; // cas a datum odletu
    private RunwayType  runwayType;
    private Runway runway;
    private int priority;
    private Airplane airplane;

    public Flight(Airplane airplane, int priority) {
        this.priority = priority;
        this.airplane = airplane;
        this.runway = null;
        this.runwayType = null;
    }

    public Flight(String code) {
        this.airplane = new Airplane(code);
    }

    public RunwayType getRunwayType() {
        return runwayType;
    }

    public void setRunwayType(RunwayType runwayType) {
        this.runwayType = runwayType;
    }

    public Runway getRunway() {
        return runway;
    }

    public void setRunway(Runway runway) {
        this.runway = runway;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getRunwayRequest() {
        return runwayRequest;
    }

    public void setRunwayRequest(LocalDateTime runwayRequest) {
        this.runwayRequest = runwayRequest;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getCode() {
        return airplane.getCode();
    }

    @Override
    public String toString() {
        return airplane + " " + priority;
    }
}
