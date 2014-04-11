package com.iKairos.trafficSim.models;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Lane;
import com.iKairos.utils.IllegalArgumentException;

/**
 * Based on the MOBIL lane change model due to Treiber et al.
 * Variables and logic used as described in the Wikipedia entry on MOBIL
 */
public class LaneChangeModel {

    public LaneChangeStatus getLaneChangeStatus(Vehicle requester) throws IllegalArgumentException {

        Lane currentLane = requester.getCurrentLane();
        Lane targetLane = currentLane.getNextLane();

        Vehicle prospectiveFollower = targetLane.getProspectiveFollower(requester);
        Vehicle follower = currentLane.getFollower(requester);
        Vehicle leader = currentLane.getLeader(requester);

        double requesterAccGain = getRequesterAccGain(requester, targetLane);
        double prospectiveFollowerAccGain = getProspectiveFollowerAccGain(requester, prospectiveFollower);
        double followerAccGain = getFollowerAccGain(leader, follower);

        double laneChangeIncentive =
                requesterAccGain + requester.getPoliteness() * (prospectiveFollowerAccGain + followerAccGain);

        double laneChangeThreshold = 0.1d;
        if (laneChangeIncentive > laneChangeThreshold) {
            if (clearanceBetween(requester, prospectiveFollower) >= SharedConstants.minJamDistance) {
                return new LaneChangeStatus(true, targetLane);
            }
        }
        return new LaneChangeStatus(false, null);
    }

    private double clearanceBetween(Vehicle requester, Vehicle prospectiveFollower) {
        return requester.getPosition() - prospectiveFollower.getPosition() - requester.getLength();
    }

    private double getRequesterAccGain(Vehicle requester, Lane targetLane) {
        Vehicle prospectiveLeader = targetLane.getProspectiveLeader(requester);
        return calculateAccelerationGain(prospectiveLeader, requester);
    }

    private double getProspectiveFollowerAccGain(Vehicle requester, Vehicle prospectiveFollower) {
        return calculateAccelerationGain(requester, prospectiveFollower);
    }

    private double getFollowerAccGain(Vehicle leader, Vehicle currentFollower) {
        return calculateAccelerationGain(leader, currentFollower);
    }

    private double calculateAccelerationGain(Vehicle leader, Vehicle vehicleToGain) {
        double accBefore = vehicleToGain.getAcceleration();
        double accAfter = SharedConstants.idm.calculateAcceleration(leader, vehicleToGain);
        return accAfter - accBefore;
    }
}
