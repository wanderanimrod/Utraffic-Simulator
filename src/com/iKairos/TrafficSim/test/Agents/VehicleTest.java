package com.iKairos.TrafficSim.test.Agents;

import com.iKairos.TrafficSim.Agents.Car;
import com.iKairos.TrafficSim.Agents.Vehicle;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
}
