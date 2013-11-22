package com.iKairos.trafficSim.models;

import com.iKairos.trafficSim.agents.Vehicle;

/**
 * Based on the Intelligent Driver Model by Treiber et al.
 * Variable names used are as described in the Intelligent Driver Model Wikipedia entry
 */
public class IDM {

    private double T;
    private double delta;
    private double so;

    public IDM() {
        T = Constants.safeTimeHeadway;
        delta = Constants.accelerationExponent;
        so = Constants.minJamDistance;
    }

    public double calculateAcceleration(Vehicle leader, Vehicle requester) {

        double v = requester.getVelocity();
        double a = requester.getMaxAcceleration();
        double b = requester.getDesiredDeceleration();
        double deltaV = v - leader.getVelocity();
        double s = leader.getPosition() - requester.getPosition() - leader.getLength();
        double sStar = so + (v*T) + ((v*deltaV) / (2*Math.sqrt(a*b)));
        return a * (1 - Math.pow((v/requester.getDesiredVelocity()), delta) - (Math.pow((sStar/s), 2)));
    }
}
