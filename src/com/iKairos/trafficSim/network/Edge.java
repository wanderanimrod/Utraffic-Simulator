package com.iKairos.trafficSim.network;

import java.util.ArrayList;

public class Edge {

    private EdgeType type = EdgeType.TWO_WAY_RURAL_ROAD;
    ArrayList<Lane> lanes = new ArrayList<Lane>();

    public Edge(EdgeType edgeType) {
        this.type = edgeType;
    }

    public int getNumberOfLanes() {
        return this.lanes.size();
    }

    public EdgeType getType() {
        return this.type;
    }

    public Lane getLane(int laneId) {
        return this.lanes.get(laneId);
    }

    public void addLane(Lane lane) {
        this.lanes.add(lane);
        lane.setParentEdge(this);
    }

    //TODO Currently only supports two-lane edges. Clients should adjust for this.
    public Lane getNextLane (Lane lane) {

        if (lane.getParentEdge().getNumberOfLanes() == 2) {
            if (lane.getId() == 0)
                return this.lanes.get(1);
            else if (lane.getId() == 1)
                return this.lanes.get(0);
            else return null;
        }
        else return null;
    }

}

