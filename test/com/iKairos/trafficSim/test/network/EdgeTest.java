package com.iKairos.trafficSim.test.network;

import com.iKairos.trafficSim.network.Edge;
import com.iKairos.trafficSim.network.EdgeType;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EdgeTest {

    @Test
    public void shouldGetNextLaneAs1IfIdOfCurrentLaneIs0OnRuralRoad() {
        Edge edge = new Edge(EdgeType.TWO_WAY_RURAL_ROAD);
        Lane lane0 = new Lane(0);
        Lane lane1 = new Lane(1);
        edge.addLane(lane0);
        edge.addLane(lane1);
        assertThat(edge.getNextLane(lane0), is(lane1));
    }
    @Test
    public void shouldGetNextLaneAs0IfIdOfCurrentLaneIs1OnRuralRoad() {
        Edge edge = new Edge(EdgeType.TWO_WAY_RURAL_ROAD);
        Lane lane0 = new Lane(0);
        Lane lane1 = new Lane(1);
        edge.addLane(lane0);
        edge.addLane(lane1);
        assertThat(edge.getNextLane(lane1), is(lane0));    }
}
