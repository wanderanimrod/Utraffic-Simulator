package com.iKairos.trafficSim.test.agents;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.trafficSim.models.LaneChangeModel;
import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.EdgeType;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class VehicleTest {

    Vehicle car, car1, car2;
    Lane lane0, lane1;
    Edge edge;

    @Before
    public void setUp() {
        edge = new Edge(EdgeType.TWO_WAY_RURAL_ROAD);
        lane0 = new Lane(0, edge);
        lane1 = new Lane(1, edge);

        car = new Car(0, lane0);
        car1 = new Car(1, lane0);
        car2 = new Car(2, lane0);
    }

    @Test
    public void shouldAddItselfToParentLaneUponInstantiation() {
        Lane lane0Mock = mock(Lane.class);
        car = new Car(1, lane0Mock);
        verify(lane0Mock).addVehicle(car);
    }

    @Test
    public void shouldSortVehiclesInDescendingOrderOfPosition() {
        car1.setPosition(100.0);
        car2.setPosition(700.0);
        List<Vehicle> cars = Arrays.asList(car1, car2);
        List<Vehicle> sortedCars = Arrays.asList(car2, car1);
        Collections.sort(cars);
        assertThat(cars, equalTo(sortedCars));
    }

    @Test
    public void shouldEquateVehiclesOfTheSameID() {
        Vehicle car3 = new Car(car1.getId(), lane0);
        assertThat(car3, equalTo(car1));
    }

    @Test
    public void shouldBeAtPositionZeroOnCreation() {
        Vehicle car = new Car(1, lane0);
        assertThat(car.getPosition(), is(0.0));
    }

    @Test
    public void shouldMaintainInsertionOrderOfCarsIfTheyAreAtTheSamePosition() {
        List<Vehicle> cars = Arrays.asList(car1, car2);
        assertThat(car1.getPosition(), is(car2.getPosition()));
        Collections.sort(cars);
        assertThat(cars, equalTo(Arrays.asList(car1, car2)));
    }

    @Test
    public void shouldLeaveCurrentLaneAfterLaneChange() {
        car = new Car(1, lane0);
        car.changeLane(lane1);
        assertThat(car.getCurrentLane(), is(lane1));
    }
}