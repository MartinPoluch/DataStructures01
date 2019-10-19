package Apk;

import java.time.LocalDateTime;

public class Airplane {

    private final String type; // nazov vyrobcu a oznacenie typu
    private final String code; // jedinecny identifikator
    private final int minRunwayLength;
    private LocalDateTime arrival; // cas a datum priletu
    private LocalDateTime runwayRequest; // cas a datum poziadania o priletovu drahu
    private LocalDateTime departure; // cas a datum odletu
    private int priority;

    public Airplane(String type, String code, int minRunwayLength, LocalDateTime arrival, int priority) {
        this.type = type;
        this.code = code;
        this.minRunwayLength = minRunwayLength;
        this.arrival = arrival;
        this.priority = priority;
    }

    public void setRunwayRequest(LocalDateTime runwayRequest) {
        this.runwayRequest = runwayRequest;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public int getMinRunwayLength() {
        return minRunwayLength;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public LocalDateTime getRunwayRequest() {
        return runwayRequest;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", minRunwayLength=" + minRunwayLength +
                ", arrival=" + arrival +
                ", runwayRequest=" + runwayRequest +
                ", departure=" + departure +
                ", priority=" + priority +
                '}';
    }
}
