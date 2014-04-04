package com.iKairos.trafficSim.test.network;

import com.iKairos.trafficSim.network.TwoLaneOneWayRoad;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.utils.*;
import com.iKairos.utils.IllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TwoLaneOneWayRoadTest {

    TwoLaneOneWayRoad edge;
    Lane lane0, lane1;

    @Before
    public void setUp() throws IllegalArgumentException {
        edge = new TwoLaneOneWayRoad();
        lane0 = new Lane(0, edge);
        lane1 = new Lane(1, edge);
    }

    @Test
    public void shouldGetNextLaneAs1IfIdOfCurrentLaneIs0OnRuralRoad() throws IllegalArgumentException {
        assertThat(edge.getNextLane(lane0), is(lane1));
    }
    @Test
    public void shouldGetNextLaneAs0IfIdOfCurrentLaneIs1OnRuralRoad() throws IllegalArgumentException {
        assertThat(edge.getNextLane(lane1), is(lane0));
    }
}
