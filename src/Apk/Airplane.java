package Apk;

public class Airplane {

    private String type; // nazov vyrobcu a oznacenie typu
    private String code; // jedinecny identifikator
    private int minRunwayLength;
    private State state;

    public Airplane(String type, String code, int minRunwayLength) {
        this.type = type;
        this.code = code;
        this.minRunwayLength = minRunwayLength;
        this.state = State.INACTIVE;
    }

    /**
     * Konstruktor pre potreby vytvorenia "fakoveho" lietadla ktore bude sluzit k vyhladavaniu.
     */
    public Airplane(String code) {
        this.code = code;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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


    @Override
    public String toString() {
        return code + " " + type + " " + minRunwayLength;
    }
}
