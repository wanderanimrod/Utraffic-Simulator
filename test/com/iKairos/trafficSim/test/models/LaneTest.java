package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LaneTest {

    Lane lane;
    Vehicle requester;
    Edge mockEdge;

    @Before
    public void setUp() {
        mockEdge = mock(Edge.class);
        lane = new Lane(1, mockEdge);
        requester = new Vehicle(1, lane);
    }

    @Test
    public void shouldAddItselfToParentEdgeOnConstruction() {
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
    public void shouldGetProspectiveLeader() {
        
    }

    @Test
    public void shouldGetNextLane() {
        lane.getNextLane();
        verify(mockEdge).getNextLane(lane);
    }
}
