package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.utils.Numbers;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
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
        assertEquals(Math.abs(acceleration), car.getMaxAcceleration());
    }

    @Test
    public void shouldNotAccelerateIfVehicleHasReachedItsDesiredVelocity() {
        car.setVelocity(Constants.desiredVelocity);
        double acceleration = calculateAcceleration();
        assertEquals(Math.abs(acceleration), 0.0);
    }

    @Test
    public void shouldRemainStationaryWhenLeaderIsNearby() {
        assertEquals(car.getVelocity(), 0.0);
        assertEquals(car.getPosition(), 0.0);
        assertEquals(Constants.dummyLeadingVehicle.getLength(), 5.0);
        Constants.dummyLeadingVehicle.setPosition(6.0);
        double acceleration = calculateAcceleration();
        assertEquals(acceleration, 0.0);
    }

    @Test
    public void shouldAccelerateNormallyWhenLeaderIsFarAwayAndDesiredVelocityIsNotAttained() {
        car.setVelocity(15.0d);
        assertTrue(car.getVelocity() < car.getDesiredVelocity());
        double acceleration = calculateAcceleration();
        assertEquals(0.700, acceleration);
    }

    @Test
    public void shouldDecelerateWhenLeadingVehicleIsNear() {
        Constants.dummyLeadingVehicle.setPosition(50);
        car.setVelocity(10);
        assertEquals(car.getPosition(), 0.0);
        double acceleration = calculateAcceleration();
        assertEquals(-0.73d, acceleration);
    }

    private double calculateAcceleration() {
        double acceleration = idm.calculateAcceleration(Constants.dummyLeadingVehicle, car);
        return Numbers.round(acceleration, 2);
    }
}
