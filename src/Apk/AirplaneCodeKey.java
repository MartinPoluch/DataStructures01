package Apk;

public class AirplaneCodeKey implements Comparable<AirplaneCodeKey>{

    private Airplane airplane;

    public AirplaneCodeKey(Airplane airplane) {
        this.airplane = airplane;
    }

    @Override
    public int compareTo(AirplaneCodeKey airplaneCodeKey) {
        return this.airplane.getCode().compareTo(airplaneCodeKey.airplane.getCode());
    }
}
