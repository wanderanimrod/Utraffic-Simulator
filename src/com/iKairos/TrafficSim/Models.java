package com.iKairos.TrafficSim;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Network.EdgeType;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Simulation.SimConstants;

/**
 * This class holds the models used to model the behaviour of the agents in the simulation.
 * <br>It holds models like the IDM and the Lane Changing models used by the vehicles in the simulation.
 * <br>Each model is held in a separate sub-class
 * @author Nimrod
 */
public class Models {

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
			
			T = SimConstants.safeTimeHeadway;
			delta = SimConstants.accelerationExponent;
			so = SimConstants.minJamDistance;
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
			
			if (acceleration < -SimConstants.maxDeceleration) {
				//acceleration = -SimConstants.maxDeceleration;
			}
			
			return acceleration;
		}
	}
	
	/**
	 * This class holds the logic and data for making lane change decisions
	 * At the center of its function is the 
	 * <a href="http://141.30.51.183/~treiber/publications/MOBIL.pdf">
	 * MOBIL lane changing model due to Treiber et al.</a>
	 * <br>For lane changing decisions on two-way roads, a modified MOBIL model due to <u>Wandera Nimrod</u>
	 * is employed.
	 */
	public class LaneChangeModel {
	
		/**
		 * This method uses <i>MOBIL</i> to determine if the requesting vehicle needs to change lane or not.
		 * @param requester The vehicle inquiring about its lane change need.
		 * @return Whether or not requester should change lane.
		 */
		public void ChangeLaneIfNecessary (Vehicle requester) {
			
			Lane currentLane = requester.getCurrentLane();
			
			if (currentLane.getParentEdge().getType() == EdgeType.NORMAL) {
				//TODO Deal with overtaking on two-way edges first because they are more common in African cities
			}			
			else if (currentLane.getParentEdge().getType() == EdgeType.ONE_WAY) {				
				
				Lane targetLane = currentLane.getNextLane();
				
				//Then, deal with lane changes on one-way edges, starting with two-lane edges first
				
				//TODO Check thread safety for these calculations.
				
				Vehicle currentFollower = currentLane.getFollower(requester);
				Vehicle prospectiveFollower = targetLane.getProspectiveFollower(requester);				

				double accCurrentFollowerBeforeLaneChange = 0.0d;
				double accCurrentFollowerAfterLaneChange = 0.0d;
				double accNewFollowerBeforeLaneChange = 0.0d;
				double accNewFollowerAfterLaneChange = 0.0d;
				
				if (prospectiveFollower != null) {
					
					accNewFollowerBeforeLaneChange = prospectiveFollower.getAcceleration();
					accNewFollowerAfterLaneChange = SimConstants.idm.calculateAcceleration(requester, prospectiveFollower);
				}
				//Else leave the acceleration at 0 so it plays no part in the lane changing decision.
				
				if (currentFollower != null) {
					
					accCurrentFollowerBeforeLaneChange = currentFollower.getAcceleration();
					accCurrentFollowerAfterLaneChange = SimConstants.idm.calculateAcceleration(currentLane.getLeadingVehicle(requester), currentFollower);					
				}
				
				double accRequesterBeforeLaneChange = requester.getAcceleration();
				double accRequesterAfterLaneChange = SimConstants.idm.calculateAcceleration(targetLane.getProspectiveLeadingVehicle(requester), requester);		
				double incentiveCriterion = accRequesterAfterLaneChange - accRequesterBeforeLaneChange 
												+ requester.getPoliteness() * (accNewFollowerAfterLaneChange - accNewFollowerBeforeLaneChange
																				+ accCurrentFollowerAfterLaneChange - accCurrentFollowerBeforeLaneChange);
				
				//u.println("Incentive to change lane for Vehicle \"" + requester.getId() + "\" = " + incentiveCriterion + "\n");
				
				if (incentiveCriterion > SimConstants.laneChangeThreshold) {
					
					//Ensure that the lane change will leave enough clearance between requester and new follower.
					if (prospectiveFollower != null) {
						
						if (requester.getPosition() - prospectiveFollower.getPosition() - requester.getLength() >= SimConstants.minJamDistance) {
							//Change Lane to target lane
							requester.changeLane(targetLane);
						}
						//Else there will not be enough clearance from the new follower after the lane change
					}
					else {
						//No new follower. Change Lane to target lane
						requester.changeLane(targetLane);
					}
				}
				//Else do nothing. Leave the vehicle in its current lane.			
			}
		}

	}

}
