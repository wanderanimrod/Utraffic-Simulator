package com.iKairos.TrafficSim.Models;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Simulation;

/**
 * This class holds the properties and methods of the Intelligent Driver Model due to Treiber et al.
 * <br>The model's main function is to determine the acceleration of a vehicle given a set of parameters
 * about its current condition of speed and the condition of its leading vehicle
 * The IDM used is as defined here: <a href = "http://en.wikipedia.org/wiki/Intelligent_driver_model">
 * M. Treiber et al., Intelligent Driver Model </a>
 * <br> An instance of this object should be created once at the start of the simulation and reused by all
 * subsequent calls to the method calculateAcceleration() to save computational resources.
 * @author Nimrod
 */
public class IDM {

		/*
		 * The variable names used here are as described in the Wikipedia article on IDM
		 * Here are the default values of the parameters that don't vary among vehicles as used in the calculation
		 */

    private double T;

    private double delta;

    private double so;

    /**
     * Initialises global IDM parameters to their default values
     * These can be adjusted using the setters in this class.
     */
    public IDM() {

        T = Simulation.SimConstants.safeTimeHeadway;
        delta = Simulation.SimConstants.accelerationExponent;
        so = Simulation.SimConstants.minJamDistance;
    }

    /**
     * Calculates the acceleration of the client vehicle using the IDM.
     *
     * @param leader The vehicle ahead of the requester vehicle
     * @param requester The vehicle whose acceleration to calculate
     * @return The acceleration of the requester vehicle
     */
    public double calculateAcceleration (Vehicle leader, Vehicle requester) {

        if (requester.getId() == 1) {
            //u.println("Initial vehicle parameters (" + vehicle.getId() + "):");
        }
        double v = requester.getVelocity();
        if (requester.getId() == 1) {
            //u.printlntb("Velocity = " + v);
        }
        double a = requester.getMaxAcceleration();
        if (requester.getId() == 1) {
            //u.printlntb("Max Acc = " + a);
        }
        double b = requester.getDesiredDeceleration();
        if (requester.getId() == 1) {
            //u.printlntb("Desired Dec = " + b);
        }
        double deltaV = v - leader.getVelocity();
        if (requester.getId() == 1) {

            //u.printlntb("Leader vel = " + leading.getVelocity());
            //u.printlntb("Leader pos = " + leading.getPosition());
            //u.printlntb("DeltaV = " + deltaV);
        }
        double s = leader.getPosition() - requester.getPosition() - leader.getLength();
			/*u.println("leaderPos (" + leader.getPosition() + "), id (" + leader.getId() + ") - requesterPos (" + requester.getPosition()
					+ ", id (" + requester.getId() + ") - leaderLen (" + leader.getLength() + ")\n");*/

			/*//Debug
			if (leader.getVelocity() == 0.0d & s < 200) {
				u.println("The clearance from static leader = " + s + " and my current position = " + requester.getPosition() + "\n");
				//throw new IllegalAccessError("\n\nWarning: Vehicle " + requester.getId() + " approaching static vehicle " + leader.getId());
			}*/

        if (requester.getId() == 1) {
            //u.printlntb("Gap s = " + s);
            //u.printlntb("Min Gap So = " + so);
            //u.printlntb("Delta = " + delta);
            //u.printlntb("T = " + T);
        }

        double sStar = so + (v*T) + ((v*deltaV) / (2*Math.sqrt(a*b)));
        if (requester.getId() == 1) {
            //u.printlntb("sStar = " + sStar);
        }
        double acceleration = a * (1 - Math.pow((v/requester.getDesiredVelocity()), delta) - (Math.pow((sStar/s), 2)));

        if (acceleration < -Simulation.SimConstants.maxDeceleration) {
            //acceleration = -SimConstants.maxDeceleration;
        }

        return acceleration;
    }
}
