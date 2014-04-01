package com.iKairos.trafficSim.agents;

import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.Lane;

public abstract class Vehicle implements Comparable<Vehicle> {

    protected int id = -1;
    protected double position = 0.0d;
    protected double velocity = 0.0d;
    protected double acceleration = 0.0d;
    protected double desiredVelocity = Constants.desiredVelocity;
    protected double maxAcceleration = Constants.maxAcceleration;
    protected Edge currentEdge;
    protected Lane currentLane;
    protected double desiredDeceleration = Constants.desiredDeceleration;
    protected double length = Constants.vehicleLength;
    protected double politeness = Constants.driverPoliteness;

    public Vehicle(int id) {
        this.id = id;
        this.position = 0.0d;
    }

    public boolean translate(double changeInTime) {

        Vehicle leadingVehicle = this.currentLane.getLeadingVehicle(this);
        this.acceleration = Constants.idm.calculateAcceleration(leadingVehicle, this);
        double newVelocity = this.velocity + this.acceleration * changeInTime;

        if (newVelocity < 0)
            newVelocity = this.velocity;

        this.velocity = newVelocity;
        double displacement = this.velocity*changeInTime;

        if (displacement < 0)
            displacement = 0;

        this.position += displacement;

        Constants.laneChangeModel.changeLaneIfNecessary(this);

        return false;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setDesiredVelocity (double velocity) {
        this.desiredVelocity = velocity;
    }

    public void setMaxAcceleration(double acceleration) {
        this.maxAcceleration = acceleration;
    }

    public void setPoliteness(double politeness) {
        this.politeness = politeness;
    }

    public void setCurrentLane(Lane lane) {
        this.currentLane = lane;
    }

    public void changeLane(Lane targetLane) {
        targetLane.insertVehicleAtItsCurrentPosition(this);
        currentLane.removeVehicle(this);
        this.setCurrentLane(targetLane);
    }

    public void setCurrentEdge(Edge edge) {
        this.currentEdge = edge;
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

    @Override
    public int compareTo(Vehicle vehicle) {
        if(this.getPosition() > vehicle.getPosition())
            return  -1;
        else if(this.getPosition() < vehicle.getPosition())
            return 1;
        else
            return 0;
    }
}
