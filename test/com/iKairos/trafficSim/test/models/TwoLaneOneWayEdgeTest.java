package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.network.TwoLaneOneWayEdge;
import com.iKairos.utils.IllegalArgumentException;
import com.iKairos.utils.IllegalMethodCallException;
import org.junit.Before;
import org.junit.Test;

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

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAskedForNextLaneInReferenceToLaneNotOnThisRoad() throws IllegalArgumentException, IllegalMethodCallException {
        Lane laneNotOnRoad = new Lane(100, new TwoLaneOneWayEdge());
        edge.getNextLane(laneNotOnRoad);
    }
}
