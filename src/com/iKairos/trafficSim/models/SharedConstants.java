package com.iKairos.trafficSim.models;

import com.iKairos.trafficSim.agents.Vehicle;

public class SharedConstants {

    public static double minJamDistance = 1.0d;
    public static IDM idm;
    public static LaneChangeModel laneChangeModel;
    public static final Vehicle dummyLeader = Vehicle.makeDummyLeader();
    public static final Vehicle dummyFollower = Vehicle.makeDummyFollower();
}
