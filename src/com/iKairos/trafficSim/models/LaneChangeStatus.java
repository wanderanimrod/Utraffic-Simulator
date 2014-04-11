package com.iKairos.trafficSim.models;


import com.iKairos.trafficSim.network.Lane;

public class LaneChangeStatus {
    public boolean shouldChangeLane;
    public Lane targetLane;

    public LaneChangeStatus(boolean shouldChangeLane, Lane targetLane) {
        this.shouldChangeLane = shouldChangeLane;
        this.targetLane = targetLane;
    }
}
