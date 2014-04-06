package com.iKairos.trafficSim.test.network;

import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.network.TwoLaneOneWayEdge;
import com.iKairos.utils.IllegalArgumentException;
import com.iKairos.utils.IllegalMethodCallException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TwoLaneOneWayEdgeTest {

    private TwoLaneOneWayEdge edge;
    private Lane lane1, lane2;

    @Before
    public void setUp() throws IllegalMethodCallException {
        edge = new TwoLaneOneWayEdge();
        lane1 = new Lane(1, edge);
        lane2 = new Lane(2, edge);
    }

    @Test(expected=IllegalMethodCallException.class)
    public void shouldRejectAdditionOfOtherLanesIfItAlreadyHasTwoLanes() throws IllegalMethodCallException {
        new Lane(3, edge);
    }

    @Test
    public void shouldGetNextLaneInAlternateFashion() throws IllegalArgumentException {
        assertThat(edge.getNextLane(lane1), equalTo(lane2));
        assertThat(edge.getNextLane(lane2), equalTo(lane1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAskedForNextLaneInReferenceToLaneNotOnThisRoad() throws IllegalArgumentException, IllegalMethodCallException {
        Lane laneNotOnRoad = new Lane(100, new TwoLaneOneWayEdge());
        edge.getNextLane(laneNotOnRoad);
    }
}
