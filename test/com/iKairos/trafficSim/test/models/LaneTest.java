package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
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
        requester = new Car(1, lane);
    }

    @Test
    public void shouldAddItselfToParentEdgeOnConstruction() {
        Lane lane = new Lane(2, mockEdge);
        verify(mockEdge).addLane(lane);
    }

    @Test
    public void shouldReturnLeaderAsAVehicleFarAwayWhenRequesterIsTheLeadingVehicleOnTheLane() {
        Vehicle leader = lane.getLeadingVehicle(requester);
        assertThat(leader, equalTo((Vehicle)new Car(-1, lane)));
        assertThat(leader.getPosition(), is(100000d));
    }

    @Test
    public void shouldGetNextLane() {
        lane.getNextLane();
        verify(mockEdge).getNextLane(lane);
    }
}
