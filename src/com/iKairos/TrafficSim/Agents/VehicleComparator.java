package com.iKairos.TrafficSim.Agents;

import java.util.Comparator;

public class VehicleComparator implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle vehicle1, Vehicle vehicle2) {

        if (vehicle1.getPosition() > vehicle2.getPosition()) {
            return -1;
        }
        else if (vehicle1.getPosition() < vehicle2.getPosition()) {
            return 1;
        }
        else return 0;
    }
}
