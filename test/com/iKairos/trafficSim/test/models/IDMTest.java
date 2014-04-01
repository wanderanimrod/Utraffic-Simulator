package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.utils.Numbers;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class IDMTest {

    IDM idm;
    Vehicle car;

    @Before
    public void setUp() {
        Constants.dummyLeadingVehicle = new Car(-1);
        Constants.dummyLeadingVehicle.setPosition(100000.0d);
        Constants.dummyLeadingVehicle.setAcceleration(0.0d);
        Constants.dummyLeadingVehicle.setVelocity(27.78d);
        idm = new IDM();
        car = new Car(0);
    }

    @Test
    public void shouldReturnMaximumAccelerationIfVehicleIsAtVelocityZeroWithLeaderFarAway() {
        double acceleration = calculateAcceleration();
        assertEquals(Math.abs(Numbers.round(acceleration, 5)), car.getMaxAcceleration());
    }

    @Test
    public void shouldReturnAccelerationZeroIfVehicleHasReachedItsDesiredVelocity() {
        car.setVelocity(Constants.desiredVelocity);
        double acceleration = calculateAcceleration();
        assertEquals(Math.abs(Numbers.round(acceleration, 5)), 0.0);
    }

    @Test
    public void shouldNotAccelerateWhenLeaderIsNearbyEvenIfDesiredVelocityIsNotReached() {
        assertEquals(car.getVelocity(), 0.0);
        assertEquals(car.getPosition(), 0.0);
        assertEquals(Constants.dummyLeadingVehicle.getLength(), 5.0);
        Constants.dummyLeadingVehicle.setPosition(6.0);
        double acceleration = calculateAcceleration();
        assertEquals(acceleration, 0.0);
    }

    private double calculateAcceleration() {
        return idm.calculateAcceleration(Constants.dummyLeadingVehicle, car);
    }
}
