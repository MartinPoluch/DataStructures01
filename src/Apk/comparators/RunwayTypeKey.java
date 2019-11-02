package Apk.comparators;

import Apk.RunwayType;

import java.util.Objects;

public class RunwayTypeKey implements Comparable<RunwayTypeKey>{

    private RunwayType runwayType;

    public RunwayTypeKey(RunwayType runwayType) {
        this.runwayType = runwayType;
    }

    @Override
    public int compareTo(RunwayTypeKey other) {
        return this.runwayType.getLength().compareTo(other.runwayType.getLength());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunwayTypeKey)) return false;
        RunwayTypeKey runwayKey = (RunwayTypeKey) o;
        return Objects.equals(this.runwayType.getLength(), runwayKey.runwayType.getLength());
    }

}
