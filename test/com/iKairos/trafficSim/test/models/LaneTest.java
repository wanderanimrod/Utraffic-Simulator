package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.TwoLaneOneWayEdge;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.utils.IllegalArgumentException;
import com.iKairos.utils.IllegalMethodCallException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LaneTest {

    Lane lane, adjacentLane;
    Vehicle requester;
    TwoLaneOneWayEdge mockEdge;

    @Before
    public void setUp() throws IllegalMethodCallException {
        mockEdge = mock(TwoLaneOneWayEdge.class);
        lane = new Lane(1, mockEdge);
        adjacentLane = new Lane(0, mockEdge);
        requester = new Vehicle(1, lane);
    }

    @Test
    public void shouldAddItselfToParentEdgeOnConstruction() throws IllegalMethodCallException {
        Lane lane = new Lane(2, mockEdge);
        verify(mockEdge).addLane(lane);
    }

    @Test
    public void shouldGetLeader() {
        Vehicle leader = new Vehicle(100, lane);
        Vehicle follower = new Vehicle(200, lane);
        assertThat(lane.getLeader(follower), equalTo(leader));
    }

    @Test
    public void shouldReturnLeaderAsAVehicleFarAwayWhenRequesterIsTheLeadingVehicleOnTheLane() {
        Vehicle leader = lane.getLeader(requester);
        assertThat(leader, equalTo(new Vehicle(-1, lane)));
        assertThat(leader.getPosition(), is(100000d));
    }

    @Test
    public void shouldGetFollower() {
        Vehicle leader = new Vehicle(100, lane);
        Vehicle follower = new Vehicle(200, lane);
        assertThat(lane.getFollower(leader), equalTo(follower));
    }

    @Test
    public void shouldGetProspectiveLeaderForVehicleOnAnotherLane() {
        Vehicle prospectiveLeader = new Vehicle(2, lane);
        prospectiveLeader.setPosition(100);
        Vehicle vehicleTryingToJoin = new Vehicle(1, adjacentLane);
        vehicleTryingToJoin.setPosition(50);
        Vehicle prospectiveLeaderReturned = lane.getProspectiveLeader(vehicleTryingToJoin);
        assertThat(prospectiveLeaderReturned, equalTo(prospectiveLeader));
    }

    @Test
    public void shouldGetProspectiveFollowerForVehicleOnAnotherLane() {
        Vehicle prospectiveFollower = new Vehicle(21, lane);
        prospectiveFollower.setPosition(20);
        Vehicle vehicleTryingToJoin = new Vehicle(1, adjacentLane);
        vehicleTryingToJoin.setPosition(50);
        Vehicle prospectiveFollowerReturned = lane.getProspectiveFollower(vehicleTryingToJoin);
        assertThat(prospectiveFollowerReturned, equalTo(prospectiveFollower));
    }

    @Test
    public void shouldGetNextLane() throws IllegalArgumentException {
        lane.getNextLane();
        verify(mockEdge).getNextLane(lane);
    }

    @Test
    public void shouldEquateLanesById() throws IllegalMethodCallException {
        Lane lane1 = new Lane(1, mockEdge);
        Lane lane2 = new Lane(1, mockEdge);
        assertThat(lane1, equalTo(lane2));
    }
}
