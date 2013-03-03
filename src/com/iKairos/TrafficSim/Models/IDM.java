package com.iKairos.TrafficSim.Models;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Simulation;

/**
 * Based on the Intelligent Driver Model by Treiber et al.
 * Variable names used are as described in the Intelligent Driver Model Wikipedia entry
 */
public class IDM {

    private double T;
    private double delta;
    private double so;

    public IDM() {
        T = Simulation.SimConstants.safeTimeHeadway;
        delta = Simulation.SimConstants.accelerationExponent;
        so = Simulation.SimConstants.minJamDistance;
    }

    public double calculateAcceleration (Vehicle leader, Vehicle requester) {

        double v = requester.getVelocity();
        double a = requester.getMaxAcceleration();
        double b = requester.getDesiredDeceleration();
        double deltaV = v - leader.getVelocity();
        double s = leader.getPosition() - requester.getPosition() - leader.getLength();
        double sStar = so + (v*T) + ((v*deltaV) / (2*Math.sqrt(a*b)));

        double acceleration = a * (1 - Math.pow((v/requester.getDesiredVelocity()), delta) - (Math.pow((sStar/s), 2)));

        return acceleration;
    }
}
