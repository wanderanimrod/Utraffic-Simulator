package com.iKairos.trafficSim.test.network;

import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.EdgeType;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EdgeTest {

    Edge edge;
    Lane lane0, lane1;

    @Before
    public void setUp() {
        edge = new Edge(EdgeType.TWO_WAY_RURAL_ROAD);
        lane0 = new Lane(0, edge);
        lane1 = new Lane(1, edge);
    }

    @Test
    public void shouldGetNextLaneAs1IfIdOfCurrentLaneIs0OnRuralRoad() {
        assertThat(edge.getNextLane(lane0), is(lane1));
    }
    @Test
    public void shouldGetNextLaneAs0IfIdOfCurrentLaneIs1OnRuralRoad() {
        assertThat(edge.getNextLane(lane1), is(lane0));
    }
}
