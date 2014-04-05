package com.iKairos.trafficSim.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.utils.IllegalArgumentException;

/**
 * Based on the MOBIL lane change model due to Treiber et al.
 * Variables and logic used as described in the Wikipedia entry on MOBIL
 */
public class LaneChangeModel {

    public void changeLaneIfNecessary(Vehicle requester) {

        double accCurrentFollowerBeforeLaneChange = 0.0d;
        double accCurrentFollowerAfterLaneChange = 0.0d;
        double accNewFollowerBeforeLaneChange = 0.0d;
        double accNewFollowerAfterLaneChange = 0.0d;

        Lane currentLane = requester.getCurrentLane();

        Lane targetLane = null;
        try {
            targetLane = currentLane.getNextLane();
        } catch (IllegalArgumentException ignored) {}

        Vehicle currentFollower = currentLane.getFollower(requester);
        Vehicle prospectiveFollower = targetLane.getProspectiveFollower(requester);

        if (prospectiveFollower != null) {
            accNewFollowerBeforeLaneChange = prospectiveFollower.getAcceleration();
            accNewFollowerAfterLaneChange = SharedConstants.idm.calculateAcceleration(requester, prospectiveFollower);
        }

        if (currentFollower != null) {
            accCurrentFollowerBeforeLaneChange = currentFollower.getAcceleration();
            accCurrentFollowerAfterLaneChange =
                    SharedConstants.idm.calculateAcceleration(currentLane.getLeader(requester), currentFollower);
        }

        double accRequesterBeforeLaneChange = requester.getAcceleration();

        double accRequesterAfterLaneChange =
                SharedConstants.idm.calculateAcceleration(targetLane.getProspectiveLeader(requester), requester);

        double incentiveCriterion = accRequesterAfterLaneChange - accRequesterBeforeLaneChange
                + requester.getPoliteness() * (accNewFollowerAfterLaneChange - accNewFollowerBeforeLaneChange
                + accCurrentFollowerAfterLaneChange - accCurrentFollowerBeforeLaneChange);

        //TODO Put this clearance check in another method
        double laneChangeThreshold = 0.1d;
        if (incentiveCriterion > laneChangeThreshold) {
            if (prospectiveFollower != null) {

                if (requester.getPosition() - prospectiveFollower.getPosition() - requester.getLength()
                        >= SharedConstants.minJamDistance) {
                    requester.changeLane(targetLane);
                }
            } else {
                requester.changeLane(targetLane);
            }
        }
    }
}
