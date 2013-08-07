package com.iKairos.trafficSim.city;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Network;

import java.util.List;

public class City {

    Network roadNetwork;
    List<Vehicle> vehicles;

    public City(Network network, List<Vehicle> vehicles) {
        this.roadNetwork = network;
        this.vehicles = vehicles;
    }
}
