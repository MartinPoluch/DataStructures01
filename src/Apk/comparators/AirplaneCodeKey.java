package Apk.comparators;

import Apk.Airplane;

import java.util.Objects;

public class AirplaneCodeKey implements Comparable<AirplaneCodeKey>{

    private Airplane airplane;

    public AirplaneCodeKey(Airplane airplane) {
        this.airplane = airplane;
    }

    @Override
    public int compareTo(AirplaneCodeKey airplaneCodeKey) {
        return this.airplane.getCode().compareTo(airplaneCodeKey.airplane.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirplaneCodeKey)) return false;
        AirplaneCodeKey that = (AirplaneCodeKey) o;
        return Objects.equals( this.airplane.getCode(), that.airplane.getCode());
    }


}
