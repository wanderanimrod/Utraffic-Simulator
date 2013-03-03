package com.iKairos.TrafficSim;

import com.iKairos.TrafficSim.Agents.Car;
import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Models.IDM;
import com.iKairos.TrafficSim.Models.LaneChangeModel;
import com.iKairos.TrafficSim.Network.Edge;
import com.iKairos.TrafficSim.Network.EdgeType;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Network.Network;
import com.iKairos.Utils.u;
import com.iKairos.Utils.u.CSV;

import java.util.ArrayList;
import java.util.Date;


// TODO: Auto-generated Javadoc
/**
 * Represents the currently running simulation.
 *
 * @author Nimrod
 */
public class Simulation {
	
	/** The vehicles. */
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	/** The network. */
	Network network;
	
	/**
	 * Holds globally accessible simulation constants. 
	 * <br>These are only the preferred constants. Each agent should be able to override these constants.
	 * <br>Metric units are used. That is, distance is measured in meters, acceleration in meters per second squared, etc
	 * @author Nimrod
	 */
	public static class SimConstants {
		
		//IDM Parameters and their acceptable values
		/** The max acceleration. */
		public static double maxAcceleration = 0.73d;
		
		/** The max deceleration. */
		public static double maxDeceleration = 9.0d;
		
		/** The desired deceleration for vehicles. */
		public static double desiredDeceleration = 1.67d;
		
		/** The acceleration exponent used in the IDM Car following model. */
		public static double accelerationExponent = 4.0d;
		
		/** The minimum jam distance  for vehicles. */
		public static double minJamDistance = 1.0d;
		
		/** The desired velocity for vehicles. */
		public static double desiredVelocity = 33.33d;
		
		/** The safe time headway. */
		public static double safeTimeHeadway = 1.6d;
		
		/** The vehicle length. */
		public static double vehicleLength = 5.0d;
		
		/** The driver politeness for vehicles. */
		public static double driverPoliteness = 0.5d;
		
		/** The desired lane for vehicles. */
		public static int desiredLane = 0;
		
		/** The Car following model used by vehicles in the simulation. */
		public static IDM idm;
		
		/** The lane change model used by vehicles in the simulation. */
		public static LaneChangeModel laneChangeModel;
		
		/** A vehicle returned as the leading vehicle for all leading vehicles on a lane. */
		public static Car dummyLeadingVehicle;
		
		/** The RHS value if the MOBIL lane change */
		public static double laneChangeThreshold = 0.1d;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		Simulation simulation = new Simulation();
		simulation.start();
	}

	/**
	 * Instantiates some globally used variables in the simulation.
	 */
	private void instantiate() {
		
		/*
		 * Instantiate the Car-following model. This is done here so we avoid having an instance of
		 * IDM in memory for every vehicle in the simulation.
		 */
		SimConstants.idm = new IDM();
		SimConstants.laneChangeModel = new LaneChangeModel();
		
		/**
		 * Create dummy vehicle that will be used by all leading vehicles as the leader for purposes of calculating
		 * vehicle accelerations using IDM
		 */
		//Return a vehicle that is 100km away from any requester and moving at 100km/h (or 28m/s)
		SimConstants.dummyLeadingVehicle = new Car(-1);
		SimConstants.dummyLeadingVehicle.setPosition(100000.0d); //The position should be >> the length of the longest edge
		SimConstants.dummyLeadingVehicle.setAcceleration(0.0d);
		SimConstants.dummyLeadingVehicle.setVelocity(27.78d);
	}
	
	/**
	 * Populates the road network from a Network Description File.
	 * @return The fully created Network
	 */
	private void createNetwork() {
		
		//Create and populate network
		network = new Network();
		Edge edge = new Edge(0, EdgeType.ONE_WAY_MULTIPLE_LANES, 1000);
		//Edge edge = new Edge(0, EdgeType.TWO_WAY_RURAL_ROAD, 1000);
		edge.addLane(new Lane(0));
		edge.addLane(new Lane(1));
		network.addEdge(edge);
		network.optimise();
	}
	
	/**
	 * Creates vehicles as stipulated in the vehicle specification file and inserts them into the simulation
	 * <br>Each Vehicle knows its route through the network, referencing it OD Matrix.
	 * <br>The moment a vehicle is inserted, it joins the queue of vehicles at the edge
	 * to which the vehicle is to be added.
	 */
	private void createVehicles() {
		
		Car car0 = new Car(0);
		/*car0.setDesiredVelocity(0.1d);
		car0.setMaxAcceleration(1.0d);*/
		car0.setPoliteness(0);
		this.vehicles.add(car0);
		
		Car car1 = new Car(1);
		car1.setDesiredVelocity(40.0d);
		car1.setMaxAcceleration(1.0d);
		car1.setPoliteness(0);
		this.vehicles.add(car1);
		
		Car car2 = new Car(2);
		car2.setDesiredVelocity(50.0d);
		car2.setMaxAcceleration(1.0d);
		car2.setPoliteness(0);
		this.vehicles.add(car2);
		
		//Introduce log in lane 0 to block car 3 and see how he reacts
		Car car3 = new Car(3);
		car3.setVelocity(0.0d);
		car3.setPosition(500.0d);
		this.vehicles.add(car3);
		
		//Introduce another blockage in lane 1 10 meters ahead of the blockage in lane 0 to see how cars maneuver about this.
		
		//Insert the vehicles into the network
		network.getEdge(0).getLane(0).insertVehicleAtItsCurrentPosition(car3);
		network.getEdge(0).getLane(0).insertVehicleAtTheStartOfTheLane(car0);
		network.getEdge(0).getLane(1).insertVehicleAtTheStartOfTheLane(car1);
		network.getEdge(0).getLane(0).insertVehicleAtTheStartOfTheLane(car2);
		
	}
	
	/**
	 * Makes the agents in the simulation move and the sensors start listening for events.
	 * <br>Multi threaded.
	 * @param refreshRate <i>(in milliseconds)<i> The time interval between individual refreshes of the simulation state
	 */
	private void play (final int refreshRate) {
		
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		    	vehicles.get(0).translate(refreshRate);
		    }
		},0, refreshRate);*/
		
		CSV output = new CSV(new String[] {
				"Position Car1", "Acceleration Car1", "Velocity Car1", "Lane Car1",				
				"Position Car2", "Acceleration Car2", "Velocity Car2", "Lane Car2",
				"Position Car3", "Acceleration Car3", "Velocity Car3", "Lane Car3"},
				"MOBIL Testing");
		
		//For debug, update vehicle properties in loop
		Date startTime = new Date();
		
		for (int i = 0; i < 700; i++) {
			
			/*if (vehicles.get(0).translate(refreshRate/1000.0d) == true) {
				
				//Remove vehicle from lane if it can be inserted into its next lane
				vehicles.get(0).getCurrentLane().removeLeadingVehicle();
				
				//TODO Insert the vehicle into its next lane in the vehicle's OD matrix
				
				break;
			}
			//Else continue translating
			 */
			u.println("Translation " + i + "\n");
			
			//if (i < 350)
				vehicles.get(0).translate(refreshRate/1000.0d);
			//else {
				//vehicles.get(0).setVelocity(0);
				//vehicles.get(0).setAcceleration(0);
			//}
			
			//Insert another vehicle at a later time
			if (i > 20) {
				
				vehicles.get(1).translate(refreshRate/1000.0d);
			}
			
			//Insert another vehicle at a later time
			if (i > 40) {
				
				vehicles.get(2).translate(refreshRate/1000.0d);
			}
						
			//Send vehicle params to CSV for visualisation
			output.appendToNewLine(Double.toString(vehicles.get(0).getPosition()));
			output.appendToRow(Double.toString(vehicles.get(0).getAcceleration()));
			output.appendToRow(Double.toString(vehicles.get(0).getVelocity()));
			output.appendToRow(Integer.toString(vehicles.get(0).getCurrentLane().getId()));
			output.appendToRow(Double.toString(vehicles.get(1).getPosition()));
			output.appendToRow(Double.toString(vehicles.get(1).getAcceleration()));
			output.appendToRow(Double.toString(vehicles.get(1).getVelocity()));
			output.appendToRow(Integer.toString(vehicles.get(1).getCurrentLane().getId()));
			//output.appendToRow("");
			output.appendToRow(Double.toString(vehicles.get(2).getPosition()));
			output.appendToRow(Double.toString(vehicles.get(2).getAcceleration()));
			output.appendToRow(Double.toString(vehicles.get(2).getVelocity()));
			output.appendToRow(Integer.toString(vehicles.get(2).getCurrentLane().getId()));
		}
		u.println("Elapsed Time: " + (new Date().getTime() - startTime.getTime() )/1000.0d + " seconds");
		u.println("Output saved = " + output.save());
	}	
	
	/**
	 * Starts the simulation.
	 */
	public void start() {
		
		instantiate();
		createNetwork();
		createVehicles();
		
		//Start the simulation and ask that it refreshes every 0.5 seconds
		//TODO Make this number smaller so vehicle get as close a possible to the end of each lane
		play(500);
	}
	
	/**
	 * Pauses the simulation.
	 */
	@SuppressWarnings("unused")
	private void pause () {
		
	}
}
