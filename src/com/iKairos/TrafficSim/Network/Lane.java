package com.iKairos.TrafficSim.Network;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Models.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lane {

    private int id;
    private double length;
    private Edge parentEdge;
    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

    public Lane(int id) {
        this.id = id;
    }

    public Vehicle getLeadingVehicle (Vehicle requester) {

        int requesterPos = this.vehicles.indexOf(requester);

        System.out.println("Vehicles currently on lane " + this.id + " are:");

        for (Vehicle vehicle: this.vehicles) {
            System.out.println("App Id = " + vehicle.getId() + " | pos = " + vehicle.getPosition() + " | vel = " + vehicle.getVelocity());
        }

        System.out.println("Requester Vehicle " + requester.getId() + " is in position " + requesterPos + " of lane " + this.id + "\n");

        if (requesterPos != 0) {
            return this.vehicles.get(requesterPos - 1);
        }
        else {
            return Constants.dummyLeadingVehicle;
        }
    }

    //TODO Should be in Vehicle. It is the vehicle that should know about its surroundings.
    //TODO Think about how it will affect performance before you refactor
    public Vehicle getFollower (Vehicle vehicle) {

        int requesterPos = vehicles.indexOf(vehicle);

        if (requesterPos != vehicles.size() - 1) {
            return vehicles.get(requesterPos + 1);
        }
        else {
            return null;
        }
    }

    public Vehicle getProspectiveLeadingVehicle (Vehicle requester) {

        ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>)vehicles.clone();

        insertVehicleAndMaintainOrder(requester, dummyVehicles);

        int requesterPos = dummyVehicles.indexOf(requester);

        if (requesterPos != 0) {
            return dummyVehicles.get(requesterPos - 1);
        }
        else {
            return Constants.dummyLeadingVehicle;
        }
    }

    public Vehicle getProspectiveFollower (Vehicle requester) {

        ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>) vehicles.clone();

        insertVehicleAndMaintainOrder(requester, dummyVehicles);

        int requesterPos = dummyVehicles.indexOf(requester);

        if (requesterPos != dummyVehicles.size() - 1) {
            return dummyVehicles.get(requesterPos + 1);
        }
        else {
            return null;
        }
    }

    public void insertVehicleAtTheStartOfTheLane (Vehicle vehicle) {

        vehicle.setPosition(0.0d);

        /*Put the vehicle at the end of the queue of vehicles.
        This must be synchronised so that before a thread finishes adding its vehicle,
        another thread cannot interrupt it. Synchronise on the current lane.*/
        synchronized (this) {
            vehicles.add(vehicle);
        }

        /**Two threads cannot try to update the properties of a single vehicle by design.
         * A single vehicle's operation will always be handled by one thread, so the
         * following block of code is thread safe.*/

        vehicle.setCurrentLane(this);
        System.out.println("Current lane for vehicle " + vehicle.getId() + " is " + this.getId());

        vehicle.setCurrentEdge(this.parentEdge);
    }

    public synchronized void insertVehicleAtItsCurrentPosition(Vehicle vehicle) {
        insertVehicleAndMaintainOrder(vehicle, this.vehicles);
    }

    public synchronized void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public int getId() {
        return this.id;
    }

    public void setParentEdge(Edge parent) {
        this.parentEdge = parent;
        this.length = parent.getLength();
    }

    public Edge getParentEdge() {

        return this.parentEdge;
    }

    //TODO Fix this to return more predictable results and not-null on edges with >2 lanes
    public Lane getNextLane() {
        return this.parentEdge.getNextLane(this);
    }

    public double getLength() {
        return this.length;
    }

    public ArrayList<Vehicle> getVehicles() {
        return this.vehicles;
    }

    /**
     * This utility method inserts a vehicle into the vehicles list of a lane at a position that keeps the list in
     * sorted order always.*/
    private void insertVehicleAndMaintainOrder(Vehicle vehicle, List<Vehicle> vehicles) {

        //TODO replace the mergeSort Collections.sort() uses with faster insertion sort for nearly sorted arrays.
        vehicles.add(vehicle);
        Collections.sort(vehicles);
    }
}
