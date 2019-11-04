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

    /**
     * Konstruktor ktory dostane ako parameter riadok suboru. Kde dany riadok reprezentuje udaje o konkretnom lietadle.
     */
    public Airplane(String[] attributes) {
        this.code = attributes[0];
        this.type = attributes[1];
        this.minRunwayLength = Integer.parseInt(attributes[2]);
        this.state = stateFromString(attributes[3]);
    }

    private State stateFromString(String strState) throws IllegalArgumentException{
        switch (strState) {
            case "ARRIVED" : {
                return State.ARRIVED;
            }
            case "WAITING" : {
                return State.WAITING;
            }
            case "ON_RUN_WAY" : {
                return State.ON_RUN_WAY;
            }
            case "INACTIVE" : {
                return State.INACTIVE;
            }
            default: {
                throw new IllegalArgumentException("Unknown state: " + strState);
            }
        }
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
        return code + "," + type + "," + minRunwayLength + "," + state + "\n";
    }
}
