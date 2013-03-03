package com.iKairos.TrafficSim.Models;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Network.EdgeType;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Simulation;

/**
 * Based on the MOBIL lane change model due to Treiber et al.
 * Variables and Logic used as described in the Wikipedia entry on MOBIL*/
//TODO Adjust this Mode or create another one that deals with Overtaking on rural roads
public class LaneChangeModel {

    public void ChangeLaneIfNecessary (Vehicle requester) {

        Lane currentLane = requester.getCurrentLane();

        if (currentLane.getParentEdge().getType() == EdgeType.TWO_WAY_RURAL_ROAD) {
            //TODO Deal with overtaking on two-way edges first because they are more common in African cities
        }
        else if (currentLane.getParentEdge().getType() == EdgeType.ONE_WAY_MULTIPLE_LANES) {

            Lane targetLane = currentLane.getNextLane();

            //TODO Check thread safety for these calculations.

            Vehicle currentFollower = currentLane.getFollower(requester);
            Vehicle prospectiveFollower = targetLane.getProspectiveFollower(requester);

            double accCurrentFollowerBeforeLaneChange = 0.0d;
            double accCurrentFollowerAfterLaneChange = 0.0d;
            double accNewFollowerBeforeLaneChange = 0.0d;
            double accNewFollowerAfterLaneChange = 0.0d;

            if (prospectiveFollower != null) {
                accNewFollowerBeforeLaneChange = prospectiveFollower.getAcceleration();
                accNewFollowerAfterLaneChange = Simulation.SimConstants.idm.calculateAcceleration(requester, prospectiveFollower);
            }
            //Else leave the acceleration at 0 so it plays no part in the lane changing decision.

            if (currentFollower != null) {
                accCurrentFollowerBeforeLaneChange = currentFollower.getAcceleration();
                accCurrentFollowerAfterLaneChange = Simulation.SimConstants.idm.calculateAcceleration(currentLane.getLeadingVehicle(requester), currentFollower);
            }

            //TODO Refactor this chain stuff out to conform to the law or Demeter
            double accRequesterBeforeLaneChange = requester.getAcceleration();
            double accRequesterAfterLaneChange = Simulation.SimConstants.idm.calculateAcceleration(targetLane.getProspectiveLeadingVehicle(requester), requester);
            double incentiveCriterion = accRequesterAfterLaneChange - accRequesterBeforeLaneChange
                    + requester.getPoliteness() * (accNewFollowerAfterLaneChange - accNewFollowerBeforeLaneChange
                    + accCurrentFollowerAfterLaneChange - accCurrentFollowerBeforeLaneChange);

            //TODO Put this clearance check in another method
            if (incentiveCriterion > Simulation.SimConstants.laneChangeThreshold) {
                if (prospectiveFollower != null) {

                    if (requester.getPosition() - prospectiveFollower.getPosition() - requester.getLength() >= Simulation.SimConstants.minJamDistance) {
                        requester.changeLane(targetLane);
                    }
                    //Else there will not be enough clearance from the new follower
                }
                else {
                    //No new follower. Change Lane to target lane
                    requester.changeLane(targetLane);
                }
            }
            //Else leave the vehicle in its current lane
        }
    }
}
