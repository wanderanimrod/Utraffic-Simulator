package com.iKairos.trafficSim.network;

import java.util.ArrayList;

public class Edge {

    private EdgeType type = EdgeType.TWO_WAY_RURAL_ROAD;
    private double length;
    private int id;
    ArrayList<Lane> lanes = new ArrayList<Lane>();

    public Edge(int id, EdgeType edgeType, double length) {
        this.id = id;
        this.length = length;
        this.type = edgeType;
    }

    public int getNumberOfLanes() {
        return this.lanes.size();
    }
    public double getLength() {
        return length;
    }

    public int getId() {
        return this.id;
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

