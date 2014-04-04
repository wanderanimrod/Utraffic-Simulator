package com.iKairos.trafficSim.models;

import com.iKairos.trafficSim.agents.Vehicle;

public class Constants {

    public static double maxAcceleration = 0.73d;
    public static double desiredDeceleration = 1.67d;
    public static double minJamDistance = 1.0d;
    public static double desiredVelocity = 33.33d;
    public static double vehicleLength = 5.0d;
    public static double driverPoliteness = 0.5d;
    public static Vehicle dummyLeadingVehicle;
    public static double laneChangeThreshold = 0.1d;
}
