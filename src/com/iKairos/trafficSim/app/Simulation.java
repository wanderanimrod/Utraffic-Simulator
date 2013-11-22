package com.iKairos.trafficSim.app;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.trafficSim.models.LaneChangeModel;
import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.EdgeType;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.network.Network;
import com.iKairos.utils.CSV;

import java.util.ArrayList;
import java.util.Date;

public class Simulation {

    ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	Network network;

	public static void main(String[] args) {
		Simulation simulation = new Simulation();
		simulation.start();
	}

    private void instantiate() {

        Constants.idm = new IDM();
		Constants.laneChangeModel = new LaneChangeModel();

		Constants.dummyLeadingVehicle = new Car(-1);
		Constants.dummyLeadingVehicle.setPosition(100000.0d); // > length of longest edge
		Constants.dummyLeadingVehicle.setAcceleration(0.0d);
		Constants.dummyLeadingVehicle.setVelocity(27.78d);
	}

    /* TODO Should be out of here into network or network service */
	private void createNetwork() {

		network = new Network();
		Edge edge = new Edge(0, EdgeType.ONE_WAY_MULTIPLE_LANES, 1000);
		edge.addLane(new Lane(0));
		edge.addLane(new Lane(1));
		network.addEdge(edge);
		network.optimise();
	}

	private void createVehicles() {
		
		Car car0 = new Car(0);
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

		Car car3 = new Car(3);
		car3.setVelocity(0.0d);
		car3.setPosition(500.0d);
		this.vehicles.add(car3);

		network.getEdge(0).getLane(0).insertVehicleAtItsCurrentPosition(car3);
		network.getEdge(0).getLane(0).insertVehicleAtTheStartOfTheLane(car0);
		network.getEdge(0).getLane(1).insertVehicleAtTheStartOfTheLane(car1);
		network.getEdge(0).getLane(0).insertVehicleAtTheStartOfTheLane(car2);
		
	}

	private void play (final int refreshRate) {

		CSV output = new CSV(new String[] {
				"Position Car1", "Acceleration Car1", "Velocity Car1", "Lane Car1",				
				"Position Car2", "Acceleration Car2", "Velocity Car2", "Lane Car2",
				"Position Car3", "Acceleration Car3", "Velocity Car3", "Lane Car3"},
				"MOBIL Testing");

        Date startTime = new Date();
		
		for (int i = 0; i < 700; i++) {

			System.out.println("Translation " + i + "\n");

            vehicles.get(0).translate(refreshRate/1000.0d);

			if (i > 20)
				vehicles.get(1).translate(refreshRate/1000.0d);

			if (i > 40)
				vehicles.get(2).translate(refreshRate/1000.0d);

			output.appendToNewLine(Double.toString(vehicles.get(0).getPosition()));

            try {
                output.appendToRow(Double.toString(vehicles.get(0).getAcceleration()));
                output.appendToRow(Double.toString(vehicles.get(0).getVelocity()));
                output.appendToRow(Integer.toString(vehicles.get(0).getCurrentLane().getId()));
                output.appendToRow(Double.toString(vehicles.get(1).getPosition()));
                output.appendToRow(Double.toString(vehicles.get(1).getAcceleration()));
                output.appendToRow(Double.toString(vehicles.get(1).getVelocity()));
                output.appendToRow(Integer.toString(vehicles.get(1).getCurrentLane().getId()));
                output.appendToRow(Double.toString(vehicles.get(2).getPosition()));
                output.appendToRow(Double.toString(vehicles.get(2).getAcceleration()));
                output.appendToRow(Double.toString(vehicles.get(2).getVelocity()));
                output.appendToRow(Integer.toString(vehicles.get(2).getCurrentLane().getId()));
            }
            catch (com.iKairos.utils.IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Elapsed Time: " + (new Date().getTime() - startTime.getTime()) / 1000.0d + " seconds");
        System.out.println("Output saved = " + output.save());
	}	

	public void start() {
		
		instantiate();
		createNetwork();
		createVehicles();

		play(500);
	}
}
