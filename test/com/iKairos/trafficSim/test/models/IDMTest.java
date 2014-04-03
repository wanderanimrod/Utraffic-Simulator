package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import org.junit.Before;
import org.junit.Test;

import static com.iKairos.trafficSim.test.helpers.Matchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


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
        assertThatResult(acceleration, isRoughly(car.getMaxAcceleration()));
    }

    @Test
    public void shouldNotAccelerateIfVehicleHasReachedItsDesiredVelocity() {
        car.setVelocity(Constants.desiredVelocity);
        double acceleration = calculateAcceleration();
        assertMagnitudeOf(acceleration, isRoughly(0));
    }

    @Test
    public void shouldRemainStationaryWhenLeaderIsNearby() {
        assertThat(car.getVelocity(), is(0.0));
        assertThat(car.getPosition(), is(0.0));
        assertThat(Constants.dummyLeadingVehicle.getLength(), is(5.0));
        Constants.dummyLeadingVehicle.setPosition(6.0);
        double acceleration = calculateAcceleration();
        assertThatResult(acceleration, isRoughly(0));
    }

    @Test
    public void shouldAccelerateNormallyWhenLeaderIsFarAwayAndDesiredVelocityIsNotAttained() {
        car.setVelocity(15.0d);
        assertThat(car.getVelocity(), lessThan(car.getDesiredVelocity()));
        double acceleration = calculateAcceleration();
        assertThatResult(acceleration, isRoughly(0.7));
    }

    @Test
    public void shouldDecelerateWhenLeadingVehicleIsNear() {
        Constants.dummyLeadingVehicle.setPosition(50);
        car.setVelocity(10);
        assertThat(car.getPosition(), is(0.0));
        double acceleration = calculateAcceleration();
        assertThatResult(acceleration, isRoughly(-0.73));
    }

    private double calculateAcceleration() {
        return idm.calculateAcceleration(Constants.dummyLeadingVehicle, car);
    }
}
