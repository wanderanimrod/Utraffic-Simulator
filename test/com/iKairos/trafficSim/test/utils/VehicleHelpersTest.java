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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;

public class VehicleHelpersTest {

    IDM idmMock;
    LaneChangeModel laneChangeMock;
    VehicleHelpers vehicleHelpers;

    @Before
    public void setUp() {
        idmMock = mock(IDM.class);
        laneChangeMock = mock(LaneChangeModel.class);
        SharedConstants.idm = idmMock;
        SharedConstants.laneChangeModel = laneChangeMock;
        vehicleHelpers = new VehicleHelpers(idmMock);
    }

    @Test
    public void shouldMoveVehicleToDesiredPosition() {
        Vehicle car = new Vehicle(1, mock(Lane.class));
        vehicleHelpers.moveVehicleToPosition(car, 10.0);
        assertThat(car.getPosition(), is(10.0));
    }

    @Test
    public void shouldFixAccelerationReturnedByIDM() {
        vehicleHelpers.fixIdmAcceleration(10);
        double acceleration = SharedConstants.idm.calculateAcceleration((Vehicle)anyObject(), (Vehicle)anyObject());
        assertThat(acceleration, is(10.0));
    }
}
