package com.iKairos.trafficSim.network;

import com.iKairos.utils.IllegalArgumentException;

public class TwoLaneOneWayRoad {

    private Lane lane1, lane2;

    public void addLane(Lane lane) throws IllegalArgumentException {
        if(lane1 == null) lane1 = lane;
        else if(lane2 == null) lane2 = lane;
        else throw new IllegalArgumentException("Two lane edge already has two lanes");
    }

    public Lane getNextLane (Lane requesterLane) throws IllegalArgumentException {
        if(lane1.equals(requesterLane)) return lane2;
        else if(lane2.equals(requesterLane)) return lane1;
        else throw new IllegalArgumentException("The lane you are on doesn't belong to this road");
    }
}

