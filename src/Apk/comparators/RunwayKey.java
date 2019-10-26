package Apk.comparators;

import Apk.RunwayType;

public class RunwayKey implements Comparable<RunwayKey>{

    private RunwayType runwayType;

    public RunwayKey(RunwayType runwayType) {
        this.runwayType = runwayType;
    }

    @Override
    public int compareTo(RunwayKey other) {
        return this.runwayType.getLength().compareTo(other.runwayType.getLength());
    }


}
