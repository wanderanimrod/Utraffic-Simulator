package com.iKairos.TrafficSim;


import java.util.Comparator;

import com.iKairos.TrafficSim.Network.Edge;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Simulation.SimConstants;
import com.iKairos.Utils.u;

// TODO: Auto-generated Javadoc
/**
 * The Class Agents.
 */
public class Agents {

	/**
	 * The Class Car.
	 */
	public static class Car extends Vehicle {

		/**
		 * Instantiates a new car.
		 *
		 * @param id the id
		 */
		public Car(int id) {
			super(id);
		}		
	}
	
	/**
	 * The Class Truck.
	 */
	public static class Truck extends Vehicle {

		/**
		 * Instantiates a new truck.
		 *
		 * @param id the id
		 */
		public Truck(int id) {
			super(id);
		}
	}
	
	/**
	 * Represents any movable object in the simulation.
	 * Cars and lane blockers should extend this.
	 * <br><br>Driver behaviour and preferences are represented as properties of the vehicle
	 * for purposes of simplicity. To reiterate, there is no distinction between a driver and
	 * the vehicle he drives in this simulation package.
	 * @author Nimrod
	 */
	public static abstract class Vehicle {
		
		//protected variables for inherited classes. 
		
		/** Each vehicle in the simulation must have an ID. */
		protected int id = -1;
		
		/** The position. */
		protected double position = 0.0d;
		
		/** The velocity. */
		protected double velocity = 0.0d;
		
		/** The acceleration. */
		protected double acceleration = 0.0d;
		
		/** The desired velocity. */
		protected double desiredVelocity = SimConstants.desiredVelocity;
		
		/** The max acceleration. */
		protected double maxAcceleration = SimConstants.maxAcceleration;
		
		/** The current edge. */
		protected Edge currentEdge;
		
		/** The current lane. */
		protected Lane currentLane;
		/**The comfortable deceleration desired by the driver. There is no such thing as maxDeceleration because
		 * this is a no-collisions-at-whatever-cost simulation.*/
		protected double desiredDeceleration = SimConstants.desiredDeceleration;
		
		/** The desired lane. */
		protected int desiredLane = SimConstants.desiredLane;	
		/**The length of the vehicle. All vehicles assume equal width, which is smaller than the width of a lane.
		 * This is a display issue that is irrelevant here.*/
		protected double length = SimConstants.vehicleLength;
		
		/** This affects the manner in which the drivers change lanes. */
		protected double politeness = SimConstants.driverPoliteness;
		
		/**
		 * Creates a vehicle and makes it ready for placement at position 0.0d of any lane onto which it will be placed
		 * @param id The Id of the vehicle.
		 */
		public Vehicle(int id) {
			
			this.id = id;
			
			//All cars start at position 0.0 of any lane they are placed on
			this.position = 0.0d;
			
			/*u.println("Here are the vehicle's parameters:");
			u.printlntb("Vehicle Id = " + this.id);
			u.printlntb("Initail position = " + this.position + "m");
			u.printlntb("Initail velocity = " + this.velocity + "m/s");
			u.printlntb("Initail acceleration = " + this.acceleration + "m/s2");
			u.printlntb("Max acceleration = " + this.maxAcceleration + "m/s2");
			u.printlntb("Desired velocity = " + this.desiredVelocity + "m/s");*/
			
		}
		
		/**
		 * Literally moves the vehicle from one position to another by assigning it a new position
		 * based on the formula <i>s = ut + (1/2)at^2</i>
		 * <br> This method also calculates the acceleration and new velocity using the IDM.
		 * <br><br><b>Vehicles actually start to change position after the first time slice because at the first time 
		 * slice, their initial acceleration is 0 and their velocity is 0, so they won't move until IDM alters their
		 * acceleration.</b>
		 * @param changeInTime The time difference between now and the last time the vehicle changed position
		 * @return Whether or not the vehicle has reached the end of the lane on which it is.
		 */
		public boolean translate(double changeInTime) {
			
			/*if (this.getCurrentLane() == null) {
				u.println("Current Lane for vehicle " + this.id + "is null\n");
			}*/
			
			//TODO Remove this assignment and replace the variable with the actual calculation where the variable is used
			Vehicle leadingVehicle = this.currentLane.getLeadingVehicle(this);
			
			/*if (leadingVehicle == null) {
				u.println("No leading vehicle returned\n");
			}
			else {
				u.println("Leading vehicle " + leadingVehicle.getId() + " is " + leadingVehicle.getPosition() +
						"m away from requester " + this.id + ", traveling at " + leadingVehicle.getVelocity() + "m/s");
			}*/
			/*if (SimConstants.idm == null) {
				u.println("IDM is null\n");
			}
			if (this.getId() == -1) {
				u.println("This car has not been instantiated. Its Id is still -1");
			}*/
			
			//Calculate new position using vehicle parameters before they are updated by IDM
			//double displacement = this.velocity*changeInTime + 0.5d*this.acceleration*Math.pow(changeInTime, 2);
			//this.position += displacement;
			//u.println("1/2 at^2 = " + 0.5d*this.acceleration*Math.pow(changeInTime, 2));
			/*u.println("t^2 = " + Math.pow(changeInTime, 2));
			u.println("a = " + this.acceleration);
			u.println("at^2 = " + this.acceleration*Math.pow(changeInTime, 2));
			u.println("New position = " + newPosition);*/
			
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
			
			//Change the vehicle's lane if necessary.
			SimConstants.laneChangeModel.ChangeLaneIfNecessary(this);
			
			
			/*//Check if the vehicle has reached the end of the lane
			if (newPosition >= this.getCurrentLane().getLength()) {
				
				u.println("Reached the end of the current lane at position " + this.position);
				
				//return true;
				
				//TODO Remove this.
				this.position = newPosition;

				u.println("New params:");
				u.printlntb("Position " + this.position);
				u.printlntb("Velocity " + this.velocity);
				u.printlntb("Acceleration " + this.acceleration + "\n");
				
				return false;
			}
			else {
				
				this.position = newPosition;

				u.println("New params:");
				u.printlntb("Position " + this.position);
				u.printlntb("Velocity " + this.velocity);
				u.printlntb("Acceleration " + this.acceleration + "\n");
				
				return false;
			}*/
			
			
			//if (this.getId() == 1) {
				
				u.println("New params for vehicle " + this.id);
				
				u.printlntb("Velocity " + this.velocity);
				u.printlntb("Acceleration " + this.acceleration);
				u.printlntb("Position " + this.position + "\n");
			//}
			
			
			return false;
		}

		
		/**
		 * Sets the velocity.
		 *
		 * @param velocity the new velocity
		 */
		public void setVelocity(double velocity) {
			this.velocity = velocity;
		}
		
		/**
		 * Sets the acceleration.
		 *
		 * @param acceleration the new acceleration
		 */
		public void setAcceleration(double acceleration) {
			this.acceleration = acceleration;
		}
		
		/**
		 * Sets the desired velocity.
		 *
		 * @param velocity the new desired velocity
		 */
		public void setDesiredVelocity (double velocity) {
			this.desiredVelocity = velocity;
		}
		
		/**
		 * Sets the max acceleration.
		 *
		 * @param acceleration the new max acceleration
		 */
		public void setMaxAcceleration(double acceleration) {
			this.maxAcceleration = acceleration;
		}
		
		/**
		 * Sets the desired deceleration.
		 *
		 * @param deceleration the new desired deceleration
		 */
		public void setDesiredDeceleration(double deceleration) {
			this.desiredDeceleration = deceleration;
		}
		
		/**
		 * Sets the desired lane.
		 *
		 * @param lane the new desired lane
		 */
		public void setDesiredLane(int lane) {
			this.desiredLane = lane;
		}
		
		/**
		 * Sets the length.
		 *
		 * @param length the new length
		 */
		public void setLength(double length) {
			this.length = length;
		}
		
		/**
		 * Sets the politeness.
		 *
		 * @param politeness the new politeness
		 */
		public void setPoliteness(double politeness) {
			this.politeness = politeness;
		}
		
		/**
		 * Sets the current lane.
		 *
		 * @param lane the new current lane
		 */
		public void setCurrentLane(Lane lane) {
			this.currentLane = lane;
		}
		
		/**
		 * Moves the vehicle from its current lane to another lane
		 * @param targetLane The lane into which the vehicle should be moved
		 */
		public void changeLane(Lane targetLane) {
			
			targetLane.insertVehicleAtCurrentPosition(this);
			currentLane.removeVehicle(this);
			this.setCurrentLane(targetLane);
			
			u.println("Vehicle " + this.id + " changed lane to " + this.getCurrentLane().getId() + "\n");
		}
		
		/**
		 * Sets the current edge of the vehicle
		 *
		 * @param edge the new  edge
		 */
		public void setCurrentEdge(Edge edge) {
			this.currentEdge = edge;
		}
		
		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}
		
		/**
		 * Gets the position.
		 *
		 * @return the position
		 */
		public double getPosition() {
			return this.position;
		}
		
		/**
		 * Gets the velocity.
		 *
		 * @return the velocity
		 */
		public double getVelocity() {
			return this.velocity;
		}
		
		/**
		 * Use the IDM to calculate the optimum acceleration.
		 *
		 * @return the acceleration
		 */	
		public double getAcceleration() {
			return this.acceleration;
		}
		
		/**
		 * Gets the politeness.
		 *
		 * @return the politeness
		 */
		public double getPoliteness() {
			return this.politeness;
		}
		
		/**
		 * Gets the desired velocity.
		 *
		 * @return the desired velocity
		 */
		public double getDesiredVelocity() {
			return this.desiredVelocity;
		}
		
		/**
		 * Gets the max acceleration.
		 *
		 * @return the max acceleration
		 */
		public double getMaxAcceleration() {
			return this.maxAcceleration;
		}
		
		/**
		 * Gets the desired deceleration.
		 *
		 * @return the desired deceleration
		 */
		public double getDesiredDeceleration() {
			return this.desiredDeceleration;
		}
			
		/**
		 * Gets the desired lane.
		 *
		 * @return the desired lane
		 */
		public int getDesiredLane() {
			return this.desiredLane;
		}
		
		/**
		 * Gets the length.
		 *
		 * @return the length
		 */
		public double getLength() {
			return this.length;
		}
		
		/**
		 * Gets the current lane.
		 *
		 * @return the current lane
		 */
		public Lane getCurrentLane() {
			return this.currentLane;
		}
		
		/**
		 * Gets the current edge.
		 *
		 * @return the current edge
		 */
		public Edge getCurrentEdge() {
			return this.currentEdge;
		}
		
		/**
		 * Sets the vehicle at a particular position in on a Lane.
		 * <br>Should be called by when a vehicle enters a new lane after a lane change 
		 * @param position The position at which to place the vehicle on the lane
		 */
		public void setPosition(double position) {
			this.position = position;
		}
	}
	
	/**
	 * Comparator for sorting vehicle lists
	 * @author Nimrod
	 */
	public class VehicleComparator implements Comparator<Vehicle> {

		@Override
		public int compare(Vehicle vehicle1, Vehicle vehicle2) {
			// TODO Auto-generated method stub
			if (vehicle1.getPosition() > vehicle2.getPosition()) {
				return -1;
			}
			else if (vehicle1.getPosition() < vehicle2.getPosition()) {
				return 1;
			}
			else return 0;							
		}
	}
}
