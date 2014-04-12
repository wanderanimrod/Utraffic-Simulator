package com.iKairos.trafficSim.agents;

import com.iKairos.trafficSim.models.SharedConstants;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.utils.*;
import com.iKairos.utils.IllegalArgumentException;

public class Vehicle implements Comparable<Vehicle> {

    protected int id = -1;
    protected double position = 0.0d;
    protected double velocity = 0.0d;
    protected double acceleration = 0.0d;
    protected double desiredVelocity = 33.33d;
    protected double maxAcceleration = 0.73d;
    protected Lane currentLane;
    protected double desiredDeceleration = 1.67d;
    protected double length = 5.0d;
    protected double politeness = 0.5d;

    public Vehicle(int id, Lane lane) {
        this.id = id;
        this.position = 0.0d;
        this.currentLane = lane;
        this.currentLane.addVehicle(this);
    }

    public Vehicle(int id, Lane lane, double desiredVelocity) {
        this(id, lane);
        this.desiredVelocity = desiredVelocity;
    }

    public static Vehicle makeDummyFollower() {
        return null;
    }

    public static Vehicle makeVehicleWithPoliteness(int id, Lane lane, double politeness) {
        Vehicle vehicle = new Vehicle(id, lane);
        vehicle.setPoliteness(politeness);
        return vehicle;
    }

    private Vehicle() {}

    public static Vehicle makeDummyLeader() {
        Vehicle dummyLeader = new Vehicle();
        dummyLeader.setPosition(100000);
        return dummyLeader;
    }

    public void translate(double changeInTime) {
        double initialVelocity = velocity;
        Vehicle leadingVehicle = currentLane.getLeader(this);
        acceleration = SharedConstants.idm.calculateAcceleration(leadingVehicle, this);

        double displacement = calculateDisplacement(changeInTime, initialVelocity);
        position += displacement;
        velocity = calculateVelocity(changeInTime, initialVelocity);

        if(velocity < desiredVelocity)
            try {
                SharedConstants.laneChangeModel.getLaneChangeStatus(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
    }

    public void changeLane(Lane targetLane) {
        targetLane.insertVehicleAtItsCurrentPosition(this);
        currentLane.removeVehicle(this);
        currentLane = targetLane;
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        if (this.getPosition() > vehicle.getPosition())
            return -1;
        else if (this.getPosition() < vehicle.getPosition())
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object vehicleToEquateWith) {
        if (this == vehicleToEquateWith) return true;
        if (!(vehicleToEquateWith instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) vehicleToEquateWith;
        return id == vehicle.id;
    }

    private double calculateVelocity(double changeInTime, double initialVelocity) {
        return initialVelocity + acceleration * changeInTime;
    }

    private double calculateDisplacement(double changeInTime, double initialVelocity) {
        return initialVelocity * changeInTime + 0.5 * (acceleration * Math.pow(changeInTime, 2));
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return this.id;
    }

    public double getPosition() {
        return this.position;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public double getPoliteness() {
        return this.politeness;
    }

    public double getDesiredVelocity() {
        return this.desiredVelocity;
    }

    public double getMaxAcceleration() {
        return this.maxAcceleration;
    }

    public double getDesiredDeceleration() {
        return this.desiredDeceleration;
    }

    public double getLength() {
        return this.length;
    }

    public Lane getCurrentLane() {
        return this.currentLane;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    private void setPoliteness(double politeness) {
        this.politeness = politeness;
    }
}
