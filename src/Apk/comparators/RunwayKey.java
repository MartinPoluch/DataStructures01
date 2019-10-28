package Apk.comparators;

import Apk.RunwayType;

import java.util.Objects;

public class RunwayKey implements Comparable<RunwayKey>{

    private RunwayType runwayType;

    public RunwayKey(RunwayType runwayType) {
        this.runwayType = runwayType;
    }

    @Override
    public int compareTo(RunwayKey other) {
        return this.runwayType.getLength().compareTo(other.runwayType.getLength());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunwayKey)) return false;
        RunwayKey runwayKey = (RunwayKey) o;
        return Objects.equals(this.runwayType.getLength(), runwayKey.runwayType.getLength());
    }

}
