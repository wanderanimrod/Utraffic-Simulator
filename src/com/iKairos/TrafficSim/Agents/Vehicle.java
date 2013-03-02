package com.iKairos.TrafficSim.Agents;

import com.iKairos.TrafficSim.Network.Edge;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Simulation.SimConstants;
import com.iKairos.Utils.u;

public abstract class Vehicle implements Comparable<Vehicle> {

    protected int id = -1;
    protected double position = 0.0d;
    protected double velocity = 0.0d;
    protected double acceleration = 0.0d;
    protected double desiredVelocity = SimConstants.desiredVelocity;
    protected double maxAcceleration = SimConstants.maxAcceleration;
    protected Edge currentEdge;
    protected Lane currentLane;
    protected double desiredDeceleration = SimConstants.desiredDeceleration;
    protected int desiredLane = SimConstants.desiredLane;
    protected double length = SimConstants.vehicleLength;
    protected double politeness = SimConstants.driverPoliteness;

    public Vehicle(int id) {

        this.id = id;
        this.position = 0.0d;
    }

    public boolean translate(double changeInTime) {

        Vehicle leadingVehicle = this.currentLane.getLeadingVehicle(this);

        this.acceleration = SimConstants.idm.calculateAcceleration(leadingVehicle, this);

        double newVelocity = this.velocity + this.acceleration * changeInTime;
        if (newVelocity < 0) {
            newVelocity = this.velocity;
        }
        this.velocity = newVelocity;

        double displacement = this.velocity*changeInTime;
        if (displacement < 0) {
            displacement = 0;
        }
        this.position += displacement;

        SimConstants.laneChangeModel.ChangeLaneIfNecessary(this);

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

    public void setDesiredDeceleration(double deceleration) {
        this.desiredDeceleration = deceleration;
    }

    public void setDesiredLane(int lane) {
        this.desiredLane = lane;
    }

    public void setLength(double length) {
        this.length = length;
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

        u.println("Vehicle " + this.id + " changed lane to " + this.getCurrentLane().getId() + "\n");
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

    public int getDesiredLane() {
        return this.desiredLane;
    }


    public double getLength() {
        return this.length;
    }

    public Lane getCurrentLane() {
        return this.currentLane;
    }

    public Edge getCurrentEdge() {
        return this.currentEdge;
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
