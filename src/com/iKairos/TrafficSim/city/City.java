package com.iKairos.TrafficSim.city;

import com.iKairos.TrafficSim.agents.Vehicle;
import com.iKairos.TrafficSim.network.Network;

import java.util.List;

public class City {

    Network roadNetwork;
    List<Vehicle> vehicles;

    public City(Network network, List<Vehicle> vehicles) {
        this.roadNetwork = network;
        this.vehicles = vehicles;
    }
}
