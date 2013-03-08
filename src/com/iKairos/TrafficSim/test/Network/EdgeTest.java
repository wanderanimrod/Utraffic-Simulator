package com.iKairos.TrafficSim.test.network;

import com.iKairos.TrafficSim.network.Edge;
import com.iKairos.TrafficSim.network.EdgeType;
import com.iKairos.TrafficSim.network.Lane;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EdgeTest {

    @Test
    public void shouldGetNextLaneAs1IfIdOfCurrentLaneIs0OnRuralRoad() {
        Edge edge = new Edge(1, EdgeType.TWO_WAY_RURAL_ROAD, 1.0);
        Lane lane0 = new Lane(0);
        Lane lane1 = new Lane(1);
        edge.addLane(lane0);
        edge.addLane(lane1);

        assertEquals(1, edge.getNextLane(lane0).getId());
    }
}
