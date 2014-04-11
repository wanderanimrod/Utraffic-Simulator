package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.trafficSim.models.LaneChangeModel;
import com.iKairos.trafficSim.models.LaneChangeStatus;
import com.iKairos.trafficSim.models.SharedConstants;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.network.TwoLaneOneWayEdge;
import com.iKairos.trafficSim.test.helpers.VehicleHelpers;
import com.iKairos.utils.IllegalArgumentException;
import com.iKairos.utils.IllegalMethodCallException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class LaneChangeModelTest {

    LaneChangeModel laneChangeModel;
    Vehicle requester, prospectiveFollower, follower;
    Lane lane1, lane0;
    VehicleHelpers helpers;
    IDM idmMock;

    @Before
    public void beforeEach() throws IllegalMethodCallException {
        idmMock = mock(IDM.class);
        SharedConstants.idm = idmMock;
        laneChangeModel = new LaneChangeModel();
        SharedConstants.laneChangeModel = laneChangeModel;

        TwoLaneOneWayEdge edge = new TwoLaneOneWayEdge();
        lane0 = new Lane(0, edge);
        lane1 = new Lane(1, edge);

        requester = new Vehicle(1, lane0);

        prospectiveFollower = new Vehicle(2, lane1);
        Lane lane1Spy = spy(lane1);
        when(lane1Spy.getProspectiveFollower(requester)).thenReturn(prospectiveFollower);

        follower = new Vehicle(3, lane0);
        Lane lane0Spy = spy(lane0);
        when(lane0Spy.getFollower(requester)).thenReturn(follower);

        helpers = new VehicleHelpers(idmMock);
    }

    @Test
    public void shouldOkayLaneChangeIfClearanceFromProspectiveFollowerAfterLaneChangeWillBeEnough() throws IllegalArgumentException {
        helpers.moveVehicleToPosition(requester, 100);
        helpers.fixIdmAcceleration(requester.getAcceleration() + 10);
        LaneChangeStatus laneChangeStatus = laneChangeModel.getLaneChangeStatus(requester);
        assertThat(getClearance(), greaterThan(SharedConstants.minJamDistance));
        assertThat(laneChangeStatus.shouldChangeLane, is(true));
        assertThat(laneChangeStatus.targetLane, equalTo(lane1));
    }

    @Test
    public void shouldNotOkayLaneChangeIfClearanceFromProspectiveFollowerAfterLaneChangeWillNotBeEnough() throws IllegalArgumentException {
        helpers.moveVehicleToPosition(requester, 5.05);
        LaneChangeStatus laneChangeStatus = laneChangeModel.getLaneChangeStatus(requester);
        assertThat(getClearance(), lessThan(SharedConstants.minJamDistance));
        assertThat(laneChangeStatus.shouldChangeLane, is(false));
    }

    @Test
    public void impoliteDriversShouldBeMoreLikelyToChangeLaneThanPoliterDrivers() {
        requester = Vehicle.makeVehicleWithPoliteness(1, lane0, 0.0);
    }

    private double getClearance() {
        return requester.getPosition() - requester.getLength() - prospectiveFollower.getPosition();
    }
}