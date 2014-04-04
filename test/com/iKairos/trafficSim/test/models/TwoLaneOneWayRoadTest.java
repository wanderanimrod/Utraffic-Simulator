package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.network.Lane;
import com.iKairos.trafficSim.network.TwoLaneOneWayRoad;
import com.iKairos.utils.IllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

public class TwoLaneOneWayRoadTest {

    private TwoLaneOneWayRoad road;
    private Lane lane1, lane2;

    @Before
    public void setUp() throws IllegalArgumentException {
        road = new TwoLaneOneWayRoad();
        lane1 = new Lane(1, road);
        lane2 = new Lane(2, road);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldRejectAdditionOfOtherLanesIfItAlreadyHasTwoLanes() throws IllegalArgumentException {
        new Lane(3, road);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAskedForNextLaneInReferenceToLaneNotOnThisRoad() throws IllegalArgumentException {
        Lane laneNotOnRoad = new Lane(100, new TwoLaneOneWayRoad());
        road.getNextLane(laneNotOnRoad);
    }
}
