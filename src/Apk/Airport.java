package Apk;

import structures.SplayTree;

public class Airport {

    private SplayTree<AirplaneCodeKey, Airplane> arrivingPlanes;

    public Airport() {
        this.arrivingPlanes = new SplayTree<>();
    }

    public void addAirplane(Airplane airplane) {
        if (airplane != null) {
            arrivingPlanes.insert(new AirplaneCodeKey(airplane), airplane);
        }
        System.out.println(arrivingPlanes.getSize());
    }
}
