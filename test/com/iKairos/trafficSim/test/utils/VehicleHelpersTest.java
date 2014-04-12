package com.iKairos.trafficSim.test.utils;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.trafficSim.models.LaneChangeModel;
import com.iKairos.trafficSim.models.SharedConstants;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.test.helpers.VehicleHelpers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class VehicleHelpersTest {

    IDM idmMock;
    LaneChangeModel laneChangeMock;
    VehicleHelpers vehicleHelpers;
    Vehicle car;

    @Before
    public void setUp() {
        idmMock = mock(IDM.class);
        laneChangeMock = mock(LaneChangeModel.class);
        SharedConstants.idm = idmMock;
        SharedConstants.laneChangeModel = laneChangeMock;
        vehicleHelpers = new VehicleHelpers(idmMock);
        car = new Vehicle(1, mock(Lane.class));
    }

    @Test
    public void shouldMoveVehicleToDesiredPosition() {
        vehicleHelpers.moveVehicleToPosition(car, 10.0);
        assertThat(car.getPosition(), is(10.0));
    }

    @Test
    public void shouldRestoreFixedIdmAccelerationAfterMovingCarToAPosition() {
        vehicleHelpers.fixIdmAcceleration(10);
        vehicleHelpers.moveVehicleToPosition(car, 100);
        double accReturnedByIdm = vehicleHelpers.calculateAccelerationWithMockVehicles();
        assertThat(accReturnedByIdm, is(10.0));
    }

    @Test
    public void shouldAccelerateVehicleToSpecifiedVelocity() {
        vehicleHelpers.accelerateVehicleToVelocity(car, 25);
        assertThat(car.getVelocity(), is(25.0));
    }

    @Test
    public void shouldRestoreFixedIdmAccelerationAfterAcceleratingCarToDesiredVelocity() {
        vehicleHelpers.fixIdmAcceleration(10);
        vehicleHelpers.accelerateVehicleToVelocity(car, 120);
        double accReturnedByIdm = vehicleHelpers.calculateAccelerationWithMockVehicles();
        assertThat(accReturnedByIdm, is(10.0));
    }

    @Test
    public void shouldFixAccelerationReturnedByIDM() {
        vehicleHelpers.fixIdmAcceleration(10);
        double acceleration = vehicleHelpers.calculateAccelerationWithMockVehicles();
        assertThat(acceleration, is(10.0));
    }

    @Test
    public void shouldAccelerateVehicleToPosition() {
        vehicleHelpers.accelerateVehicleTo(car, 0.5);
        assertThat(car.getAcceleration(), is(0.5));
    }
}
