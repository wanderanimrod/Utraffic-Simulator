package com.iKairos.TrafficSim.test.agents;

import com.iKairos.TrafficSim.agents.Car;
import com.iKairos.TrafficSim.agents.Vehicle;
import com.iKairos.TrafficSim.network.Edge;
import com.iKairos.TrafficSim.network.EdgeType;
import com.iKairos.TrafficSim.network.Lane;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

        assertEquals(1, cars.get(cars.size() -1).getId());
        assertEquals(2, cars.get(0).getId());
    }

    @Test
    public void shouldLeaveCurrentLaneAfterLaneChange() {
        Vehicle vehicle = new Car(1);
        Lane lane0 = new Lane(0);
        Lane lane1 = new Lane(1);
        Edge edge = new Edge(0, EdgeType.TWO_WAY_RURAL_ROAD, 100.0);
        edge.addLane(lane0);
        edge.addLane(lane1);
        vehicle.setCurrentLane(lane0);
        vehicle.changeLane(lane1);

        assertThat(lane0.getVehicles().size(), is(0));
        assertEquals(vehicle.getCurrentLane(), lane1);
    }
}
