package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.trafficSim.models.SharedConstants;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Before;
import org.junit.Test;

import static com.iKairos.trafficSim.test.helpers.Matchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.mock;


public class IDMTest {

    Vehicle car, dummyLeader;

    @Before
    public void setUp() {
        Lane lane = mock(Lane.class);

        car = new Vehicle(0, lane);
        dummyLeader = new Vehicle(-1, lane);

        dummyLeader.setPosition(100000.0d);
        dummyLeader.setVelocity(27.78d);
        checkThatTestDataStatusIsOkay();
    }

    @Test
    public void shouldReturnMaximumAccelerationIfVehicleIsAtVelocityZeroWithLeaderFarAway() {
        double acceleration = calculateAcceleration();
        assertThatResult(acceleration, isRoughly(car.getMaxAcceleration()));
    }

    @Test
    public void shouldNotAccelerateIfVehicleHasReachedItsDesiredVelocity() {
        car.setVelocity(33.33d);
        double acceleration = calculateAcceleration();
        assertThatMagnitudeOf(acceleration, isRoughly(0));
    }

    @Test
    public void shouldRemainStationaryWhenLeaderIsNearby() {
        dummyLeader.setPosition(6.0);
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
        dummyLeader.setPosition(50);
        car.setVelocity(10);
        double acceleration = calculateAcceleration();
        assertThatResult(acceleration, isRoughly(-0.73));
    }

    @Test
    public void shouldReturnZeroInsteadOfNaNIfVehicleDesiredVelocityIs0() {
        car = new Vehicle(1, mock(Lane.class), 0.0);
        double acceleration = calculateAcceleration();
        assertThat(acceleration, is(0.0));
    }

    private double calculateAcceleration() {
        return SharedConstants.idm.calculateAcceleration(dummyLeader, car);
    }

    private void checkThatTestDataStatusIsOkay() {
        assertThat(car.getCurrentLane(), equalTo(dummyLeader.getCurrentLane()));
        assertThat(dummyLeader.getLength(), is(5.0));
        assertThat(dummyLeader.getAcceleration(), is(0.0));
    }
}
