package com.iKairos.trafficSim.test.agents;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.EdgeType;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class VehicleTest {
    @Test
    public void shouldSortVehiclesInDescendingOrderOfPosition() {

        List<Vehicle> cars = new ArrayList<Vehicle>();
        Vehicle car1 = new Car(1);
        Vehicle car2 = new Car(2);
        Vehicle car3 = new Car(3);

        car1.setPosition(100.0);
        car2.setPosition(700.0);
        car3.setPosition(300.0);

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        Collections.sort(cars);
        assertThat(cars.get(cars.size() - 1).getId(), is(1));
        assertThat(cars.get(0).getId(), is(2));
    }

    @Test
    public void shouldLeaveCurrentLaneAfterLaneChange() {
        Vehicle vehicle = new Car(1);
        Lane lane0 = new Lane(0);
        Lane lane1 = new Lane(1);
        Edge edge = new Edge(EdgeType.TWO_WAY_RURAL_ROAD);
        edge.addLane(lane0);
        edge.addLane(lane1);
        vehicle.setCurrentLane(lane0);

        vehicle.changeLane(lane1);

        assertThat(vehicle.getCurrentLane(), is(lane1));
    }
}
