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
        System.out.println("REQ = " + requesterAccGain);

        double prospectiveFollowerAccGain = getProspectiveFollowerAccGain(requester, prospectiveFollower);
        System.out.println("PROSP = " + prospectiveFollowerAccGain);

        double followerAccGain = getFollowerAccGain(leader, follower);
        System.out.println("FOLLOWER = "  + followerAccGain);

        double laneChangeIncentive =
                requesterAccGain + requester.getPoliteness() * (prospectiveFollowerAccGain + followerAccGain);

        double laneChangeThreshold = 0.1d;
        System.out.println("LCI = " + laneChangeIncentive);
        if (laneChangeIncentive > laneChangeThreshold) {
            System.out.println("CLEARANCE = " + clearanceBetween(requester, prospectiveFollower));
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
        System.out.println("\nACC BEFORE = " + accBefore);
        double accAfter = SharedConstants.idm.calculateAcceleration(leader, vehicleToGain);
        System.out.println("ACC AFTER = " + accAfter);
        return accAfter - accBefore;
    }
}
