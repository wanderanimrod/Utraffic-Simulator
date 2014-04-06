package com.iKairos.trafficSim.network;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.utils.IllegalArgumentException;
import com.iKairos.utils.IllegalMethodCallException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lane {

    private int id;
    private TwoLaneOneWayEdge parentEdge;
    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private Vehicle dummyLeader;
    private Vehicle dummyFollower;

    public Lane(int id, TwoLaneOneWayEdge parentEdge) throws IllegalMethodCallException {
        this.id = id;
        this.parentEdge = parentEdge;
        parentEdge.addLane(this);
        this.dummyLeader = new Vehicle(-1, this);
        this.dummyLeader.setPosition(100000d);
        this.dummyFollower = new Vehicle(-2, this, 0.0);
    }

    public synchronized Vehicle getLeader(Vehicle requester) {
        int requesterPos = this.vehicles.indexOf(requester);
        if (requesterPos != 0)
            return this.vehicles.get(requesterPos - 1);
        else return this.dummyLeader;
    }

    public synchronized Vehicle getFollower (Vehicle vehicle) {
        int requesterPos = vehicles.indexOf(vehicle);
        if (requesterPos != vehicles.size() - 1)
            return vehicles.get(requesterPos + 1);
        else return this.dummyFollower;
    }

    public synchronized Vehicle getProspectiveLeader(Vehicle requester) {
        ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>)vehicles.clone();
        insertVehicleAndMaintainOrder(requester, dummyVehicles);
        int requesterPos = dummyVehicles.indexOf(requester);

        if (requesterPos != 0)
            return dummyVehicles.get(requesterPos - 1);
        else return dummyLeader;
    }

    public synchronized Vehicle getProspectiveFollower (Vehicle requester) {
        ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>)vehicles.clone();
        insertVehicleAndMaintainOrder(requester, dummyVehicles);
        int requesterPos = dummyVehicles.indexOf(requester);

        if (requesterPos != dummyVehicles.size() - 1)
            return dummyVehicles.get(requesterPos + 1);
        else return this.dummyFollower;

    }

    public void insertVehicleAtItsCurrentPosition(Vehicle vehicle) {
        insertVehicleAndMaintainOrder(vehicle, this.vehicles);
    }

    public synchronized void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public Lane getNextLane() throws IllegalArgumentException {
        return this.parentEdge.getNextLane(this);
    }

    private synchronized void insertVehicleAndMaintainOrder(Vehicle vehicle, List<Vehicle> vehicles) {
        vehicles.add(vehicle);
        Collections.sort(vehicles);
    }

    public synchronized void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    @Override
    public boolean equals(Object otherLane) {
        if (this == otherLane) return true;
        if (!(otherLane instanceof Lane)) return false;
        Lane lane = (Lane) otherLane;
        return id == lane.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
