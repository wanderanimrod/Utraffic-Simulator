package com.iKairos.TrafficSim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Simulation.SimConstants;
import com.iKairos.Utils.u;

/**
 * The variables, methods and fields about the whole Network in general.
 *
 * @author Nimrod
 */
public class Network {
	
	/** The array of edges in the network. */
	Edge[] edges;
	
	/** The edges al. */
	private ArrayList<Edge> edgesAL = new ArrayList<Edge>();
	/**The type of edge. This tells us whether the edge carries two-way traffic or not.
	 * <br>This is sometimes used as a categoriser for Rural and Urban edges*/
	enum EdgeType {
		
		/** This refers to an edge that has two lanes carrying traffic in opposite directions. */
		NORMAL,
		
		/** This refers to an edge that has one or more lanes, with all lanes carrying traffic in one direction. */
		ONE_WAY}
	
	/**
	 * The Enum LaneGradient.
	 */
	enum LaneGradient {
			/** The up-hill. */
			UPHILL, 
			 /** The down-hill. */
			 DOWNHILL
		 }
	
	/**
	 * Prints out the status of the network to the main output terminal.
	 * @return the status
	 */
	public void getStatus() {
		u.printlntb("Test");
	}
	
	/**
	 * Inserts an edge into the network.
	 * @param edge The Edge to insert into the network
	 */
	public void addEdge(Edge edge) {
		
		edgesAL.add(edge);
	}
	
	/**
	 * Gets the edge.
	 * @param edgeId the edge id
	 * @return the edge
	 */
	public Edge getEdge(int edgeId) {
		return this.edges[edgeId];
	}
	
	/**
	 * This method converts all inefficient objects in the network to their more efficient equivalents
	 * <br>For instance, it converts all ArrayLists used in the creation of the Network into Arrays.
	 */
	public void finalise() {
		
		//Convert edges ArrayList into an array for quicker access during vehicle translations		
		edges = new Edge[edgesAL.size()];
		
		for (int i = 0; i < edgesAL.size(); i++) {
			edges[i] = edgesAL.get(i);
		}
	}
	
	/**
	 * This represents an edge in the road network.
	 * <br>It doesn't necessarily equate to a real world road. A road can be made up of more than one edge.
	 * @author Nimrod
	 */	
	public static class Edge {		
		
		/** Whether the edge is one-way or not. */
		private EdgeType type = EdgeType.NORMAL;
		
		/** The length. */
		private double length;
		
		/** The number of lanes. */
		private int numberOfLanes;
		
		/** The id. */
		private int id;
		
		/** The lanes. */
		ArrayList<Lane> lanes = new ArrayList<Lane>();
		
		/**
		 * Instantiates a new edge.
		 * @param id the id
		 * @param edgeType the edge type
		 * @param length the length
		 */
		public Edge(int id, EdgeType edgeType, double length) {
			this.id = id;
			this.type = edgeType;
			this.length = length;
		}
		
		/**
		 * Gets the number of lanes.
		 * @return the number of lanes
		 */
		public int getNumberOfLanes() {
			return this.numberOfLanes;
		}
		
		/**
		 * Gets the length.
		 * @return the length
		 */
		public double getLength() {
			return length;
		}
		
		/**
		 * Gets the id.
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}
		
		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public EdgeType getType() {
			return this.type;
		}
		
		/**
		 * Sets the type.
		 *
		 * @param type the new type
		 */
		public void setType(EdgeType type) {
			this.type = type;
		}
		
		/**
		 * Gets the lane.
		 *
		 * @param laneId the lane id
		 * @return The lane with id = laneId as a Lane object.
		 */
		public Lane getLane(int laneId) {
			return this.lanes.get(laneId);
		}
		
		/**
		 * Adds a lane to the Edge.
		 * <br>This method adds lanes in the order of left-to-right in case the road is one-way
		 * or just naturally if the edge is a two-lane two-way road
		 *
		 * @param lane the lane
		 */
		public void addLane(Lane lane) {
			
			this.lanes.add(lane);
			lane.setParentEdge(this);
		}
		
		/**
		 * TODO Currently only supports two-lane edges. Clients should adjust for this.
		 * @param lane The lane whose neighbour we need to know
		 * @return The the lane neighbouring <i>lane</i> on this edge.
		 */
		public Lane getNextLane (Lane lane) {
			
			if (lane.getParentEdge().getNumberOfLanes() < 2) {
				if (lane.getId() == 0)
					return this.lanes.get(1);
				else if (lane.getId() == 1)
					return this.lanes.get(0);
				else return null;
			}
			else return null;
		}
	}
	
	/**
	 * This represents a lane
	 * <br>Theoretically, an edge can have 1 or more Lanes. 
	 * <br>It is in lanes that vehicles are held.
	 * <br><br> The way vehicles are queried should be 
	 * carefully thought out because cars will be polling the cars collection of each lane
	 * very often (about twice every second), so this polling must be computationally very efficient
	 * @author Nimrod
	 */
	public static class Lane {
		
		/** The id of this lane. */
		private int id;
		
		/** The length of this lane. */
		private double length;
		
		/** The parent edge. */
		private Edge parentEdge;
		
		/** The vehicles on this lane, always sorted by their positions on the lane*/
		private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		/**
		 * Instantiates a new lane.
		 * @param id the id
		 */
		public Lane(int id) {
			this.id = id;			
		}
		
		/**
		 * @param vehicle The Vehicle requesting the services of this method
		 * @return The vehicle ahead of requester in this lane, <b>null<b> if the requester is the
		 * leading vehicle on the lane. Client code should appropriately handle the null response.
		 */
		public Vehicle getLeadingVehicle (Vehicle requester) {
			
			int requesterPos = this.vehicles.indexOf(requester);
			
			u.println("Vehicles currently on lane " + this.id + " are:");
			for (Vehicle vehicle: this.vehicles) {
				u.println("App Id = " + vehicle.id + " | pos = " + vehicle.getPosition() + " | vel = " + vehicle.getVelocity());
			}
			u.println("Requester Vehicle " + requester.getId() + " is in position " + requesterPos + " of lane " + this.id + "\n");
			//u.println("Requester is object \"" + requester + "\"; Array has object \"" + this.vehicles.get(0) + "\"\n");
			
			if (requesterPos != 0) {
				
				return this.vehicles.get(requesterPos - 1);
			}
			else {
				
				//The requester is the leading vehicle in the lane				
				//u.println("No leading vehicle for requester\n");
				/*
				u.println("Dummy Leader has params:");
				u.printlntb("Pos " + SimConstants.dummyLeadingVehicle.getPosition() + "m");
				u.printlntb("Vel " + SimConstants.dummyLeadingVehicle.getVelocity() + "m/s");
				u.printlntb("Acc " + SimConstants.dummyLeadingVehicle.getAcceleration() + "m/s2");*/
				
				
				return SimConstants.dummyLeadingVehicle;
			}
		}
		
		/**
		 * @param vehicle The Vehicle requesting the services of this method
		 * @return The vehicle behind of requester in this lane, <b>null</b> if the requester is the
		 * last vehicle on the lane. Client code should appropriately handle the null response.
		 */
		public Vehicle getFollower (com.iKairos.TrafficSim.Agents.Vehicle vehicle) {
			
			int requesterPos = vehicles.indexOf(vehicle);
			
			if (requesterPos != vehicles.size() - 1) {				
				return vehicles.get(requesterPos + 1);
			}
			else {
				
				//The requester is the leading vehicle in the lane				
				//u.println("No leading vehicle for requester\n");
				/*
				u.println("Dummy Leader has params:");
				u.printlntb("Pos " + SimConstants.dummyLeadingVehicle.getPosition() + "m");
				u.printlntb("Vel " + SimConstants.dummyLeadingVehicle.getVelocity() + "m/s");
				u.printlntb("Acc " + SimConstants.dummyLeadingVehicle.getAcceleration() + "m/s2");*/
				
				return null;
			}
		}
		
		/**
		 * @param vehicle The Vehicle requesting the services of this method
		 * @return The vehicle that will be ahead of requester if the requester changes lanes into this lane,
		 * <b>null</b> if the requester will be the leading vehicle on the lane. 
		 * Client code should appropriately handle the null response.
		 * <br><br>This method is here specially to handle lane change operations. Making <i>getLeadingVehicle()</i>
		 * handle both cases would introduce unnecessary computational overhead when getting leading vehicles in 
		 * instances where the requester is not changing lanes.
		 */
		@SuppressWarnings("unchecked")
		public Vehicle getProspectiveLeadingVehicle (com.iKairos.TrafficSim.Agents.Vehicle requester) {
			
			//Create a dummy copy of vehicles on this lane into which the requester can be superficiously inserted.
			ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>)vehicles.clone();
			
			//Insert the requester without distorting the sorted order of the dummy array, in effect placing it at
			//the position it would be in on the new lane if it changed lanes.
			
			//Debug
			/*u.println("Vehicles on target lane are:");
			for (Vehicle vehicle : dummyVehicles) {
				u.println("Id = " + vehicle.getId() + " | pos = " + vehicle.getPosition() + " | vel = " + vehicle.getVelocity() + " | lane = " + vehicle.getCurrentLane().getId());
			}*/
			
			insertAndMaintainOrder(requester, dummyVehicles);
			
			//Debug
			/*u.println("Vehicles on target lane if requester changes lane:");
			for (Vehicle vehicle : dummyVehicles) {
				u.println("Id = " + vehicle.getId() + " | pos = " + vehicle.getPosition() + " | vel = " + vehicle.getVelocity()+ " | lane = " + vehicle.getCurrentLane().getId());
			}*/
			
			int requesterPos = dummyVehicles.indexOf(requester);
			
			if (requesterPos != 0) {				
				return dummyVehicles.get(requesterPos - 1);
			}
			else {
				
				//The requester is the leading vehicle in the lane				
				//u.println("No leading vehicle for requester\n");
				/*
				u.println("Dummy Leader has params:");
				u.printlntb("Pos " + SimConstants.dummyLeadingVehicle.getPosition() + "m");
				u.printlntb("Vel " + SimConstants.dummyLeadingVehicle.getVelocity() + "m/s");
				u.printlntb("Acc " + SimConstants.dummyLeadingVehicle.getAcceleration() + "m/s2");*/
				
				return SimConstants.dummyLeadingVehicle;
			}
		}
		
		/**
		 * @param vehicle The Vehicle requesting the services of this method
		 * @return The vehicle that will be behind of requester if the requester changes lanes into this lane,
		 * <b>null</b> if the requester will be the last vehicle on the lane. 
		 * Client code should appropriately handle the null response.
		 */
		@SuppressWarnings("unchecked")
		public Vehicle getProspectiveFollower (com.iKairos.TrafficSim.Agents.Vehicle requester) {
			
			//Create a dummy copy of vehicles on this lane into which the requester can be superficiously inserted.
			ArrayList<Vehicle> dummyVehicles = (ArrayList<Vehicle>) vehicles.clone();
			
			//Insert the requester without distorting the sorted order of the dummy array, in effect placing it at
			//the position it would be in on the new lane if it changed lanes.
			insertAndMaintainOrder(requester, dummyVehicles);
			
			int requesterPos = dummyVehicles.indexOf(requester);
			
			if (requesterPos != dummyVehicles.size() - 1) {
				return dummyVehicles.get(requesterPos + 1);
			}
			else {				
				return null;
			}
		}
		
		/**
		 * Inserts a vehicle into the lane at the start of the lane, and sets the 
		 * currentLane property of the vehicle to this lane.
		 * <br>Every vehicle is created ready to be inserted at position 0.0 of the lane
		 * <br><br>Synchronised.
		 * @param vehicle The vehicle to insert
		 */
		public void insertVehicle (Vehicle vehicle) {
			
			//Restore the vehicle's position to 0.0. Thread safe
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
			
			//Set the current lane of the vehicle to this lane
			vehicle.setCurrentLane(this);
			u.println("Current lane for vehicle " + vehicle.getId() + " is " + this.getId());
			
			//Set the current Edge of the vehicle to be the parent of this lane
			vehicle.setCurrentEdge(this.parentEdge);
			
			/**Thread safety ends here*/
		}
		
		/**
		 * Inserts a vehicle into the lane at the position at which the vehicle 
		 * currently is.
		 * <br>Should be called when a vehicle is changing lanes
		 * <br><br>Synchronised.
		 */
		public synchronized void insertVehicleAtCurrentPosition(Vehicle vehicle) {
			
			/**Find the position among vehicles at which the current vehicles insertion
			 * wont disrupt the sorted order of the vehicles list.
			 * Use a binary search like algorithm to achieve this in unbeatable average time!*/
			
			insertAndMaintainOrder(vehicle, this.vehicles);
		}
		
		/**
		 * This removes the vehicle at the front of the queue of vehicles in this lane
		 * <br>This should be called by a vehicle when it has reached the end of the lane.
 		 * <br>It leaves the array of vehicles in sorted order.
		 * <br><br>Synchronised.
		 */
		public synchronized void removeVehicle() {
			
			vehicles.remove(0);
		}
		
		/**
		 * This removes a vehicle at any position from the lane.
 		 * <br>It leaves the array of vehicles in sorted order.
		 * <br>Should be called by vehicles changing lane
		 * <br><br>Synchronised.
		 * @param vehicle The vehicle to remove from the lane
		 */
		public synchronized void removeVehicle(Vehicle vehicle) {
			
			vehicles.remove(vehicle);
		}
		
		/**
		 * @return the Id of the lane
		 */
		public int getId() {
			
			return this.id;
		}
		
		/**
		 * Sets the parent edge of the lane.
		 * <br>Should be called at the time at which the lane is added to an Edge
		 * <br>A lane is just as long as its parent edge
		 * @param parent The Edge to which this lane has been added.
		 */
		public void setParentEdge(Edge parent) {
			
			this.parentEdge = parent;
			this.length = parent.getLength();
		}
		
		/**
		 * @return The Edge to which this lane belongs
		 */
		public Edge getParentEdge() {
			
			return this.parentEdge;
		}
		
		/**
		 * @return The Lane neighbouring this lane on the same edge if there is any, null
		 * if there is no such lane, or if the Edge has more than two lanes.
		 * 
		 * <br>TODO Fix this to return more predictable results and not-null on edges with >2 lanes
		 */
		public Lane getNextLane() {
			
			return this.parentEdge.getNextLane(this);
		}
		
		/**
		 * @return The length of the lane
		 */
		public double getLength() {
			return this.length;
		}
		
		public ArrayList<Vehicle> getVehicles() {
			return this.vehicles;
		}
		
		/**
		 * This utility method inserts a vehicle into the vehicles list of a lane at a
		 * position that keeps the list in sorted order always.
		 * <br><br>Recursive.
		 * @param vehicle The vehicle to insert
		 * @param vehicles The list of vehicles <i>sorted by position</i> into which to
		 * insert <i>vehicle</i>. 
		 */
		private void insertAndMaintainOrder(Vehicle vehicle, List<Vehicle> vehicles) {
			
			/**
			 * This recursive thing was full of bugs. The sorting might not be efficient since it uses mergeSort.
			 * TODO replace it with insertion sort.
			 */
			/*int sizeOfList = vehicles.size();
			
			//Now insert the vehicle
			
			if (sizeOfList > 1) {
				
				Vehicle midVehicle = vehicles.get((int)(sizeOfList/2));
				
				if (midVehicle.getPosition() > vehicle.getPosition()) {
					insertAndMaintainOrder(vehicle, vehicles.subList(0, (int)(sizeOfList/2)));
				}
				else {
					insertAndMaintainOrder(vehicle, vehicles.subList((int)(sizeOfList/2) + 1, sizeOfList));
				}
			}
			else if (sizeOfList == 1) {
				
				if (vehicles.get(0).getPosition() >= vehicle.getPosition()) {
					vehicles.add(vehicle);
				}
				else {
					vehicles.add(0, vehicle);
				}
			}
			else {
				vehicles.add(vehicle);
			}*/
			
			vehicles.add(vehicle);
			Collections.sort(vehicles, new Agents().new VehicleComparator());
		}
	}
}
