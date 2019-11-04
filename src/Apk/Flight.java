package Apk;

import Apk.comparators.FlightPriorityKey;
import structures.HeapNode;

import java.time.LocalDateTime;

public class Flight {

    private LocalDateTime arrival; // cas a datum priletu
    private LocalDateTime runwayRequest; // cas a datum poziadania o priletovu drahu
    private LocalDateTime departure; // cas a datum odletu
    private RunwayType runwayType;
    private Runway runway;
    private int priority;
    private HeapNode<FlightPriorityKey, Flight> heapNode;
    private Airplane airplane;

    public Flight(Airplane airplane) {
        this.priority = 0;
        this.airplane = airplane;
        this.heapNode = null;
        this.runway = null;
        this.runwayType = null;
    }

    public Flight(String code) {
        this.airplane = new Airplane(code);
    }


    public void setHeapNode(HeapNode<FlightPriorityKey, Flight> heapNode) {
        this.heapNode = heapNode;
    }

    public HeapNode<FlightPriorityKey, Flight> getHeapNode() {
        return heapNode;
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

    public String getType() {
        return airplane.getType();
    }

    public Integer getMinLength() {
        return airplane.getMinRunwayLength();
    }

    private String formatDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return "," + localDateTime.toLocalDate() + " " + localDateTime.toLocalTime();
        }
        else {
            return "";
        }
    }

    @Override
    public String toString() {
        //String runwayTypeLength = (runwayType != null && airplane.getState() != State.WAITING) ? "," +  runwayType.getLength() : "";
        //String runwayId = (runway != null) ? "," + Integer.toString(runway.getId()) : "";
        // aby sa do suboru arrivedFlights.csv nezapisovala priorita. Pretoze v tomto pripade priorita vzdy bude rovna nule. Pretoze este nebola zadefinovana.
        String strPriority = (airplane.getState() == State.ARRIVED) ? "" : "," + priority;
        return getCode() + strPriority +
                formatDate(arrival) + formatDate(runwayRequest) + formatDate(departure) + "\n";
    }
}
