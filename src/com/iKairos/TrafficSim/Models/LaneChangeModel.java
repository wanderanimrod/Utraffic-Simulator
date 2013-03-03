package com.iKairos.TrafficSim.Models;

import com.iKairos.TrafficSim.Agents.Vehicle;
import com.iKairos.TrafficSim.Network.EdgeType;
import com.iKairos.TrafficSim.Network.Lane;
import com.iKairos.TrafficSim.Simulation;

/**
 * This class holds the logic and data for making lane change decisions
 * At the center of its function is the
 * <a href="http://141.30.51.183/~treiber/publications/MOBIL.pdf">
 * MOBIL lane changing model due to Treiber et al.</a>
 * <br>For lane changing decisions on two-way roads, a modified MOBIL model due to <u>Wandera Nimrod</u>
 * is employed.
 */
public class LaneChangeModel {

    /**
     * This method uses <i>MOBIL</i> to determine if the requesting vehicle needs to change lane or not.
     * @param requester The vehicle inquiring about its lane change need.
     * @return Whether or not requester should change lane.
     */
    public void ChangeLaneIfNecessary (Vehicle requester) {

        Lane currentLane = requester.getCurrentLane();

        if (currentLane.getParentEdge().getType() == EdgeType.TWO_WAY_RURAL_ROAD) {
            //TODO Deal with overtaking on two-way edges first because they are more common in African cities
        }
        else if (currentLane.getParentEdge().getType() == EdgeType.ONE_WAY_MULTIPLE_LANES) {

            Lane targetLane = currentLane.getNextLane();

            //Then, deal with lane changes on one-way edges, starting with two-lane edges first

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

            double accRequesterBeforeLaneChange = requester.getAcceleration();
            double accRequesterAfterLaneChange = Simulation.SimConstants.idm.calculateAcceleration(targetLane.getProspectiveLeadingVehicle(requester), requester);
            double incentiveCriterion = accRequesterAfterLaneChange - accRequesterBeforeLaneChange
                    + requester.getPoliteness() * (accNewFollowerAfterLaneChange - accNewFollowerBeforeLaneChange
                    + accCurrentFollowerAfterLaneChange - accCurrentFollowerBeforeLaneChange);

            //u.println("Incentive to change lane for Vehicle \"" + requester.getId() + "\" = " + incentiveCriterion + "\n");

            if (incentiveCriterion > Simulation.SimConstants.laneChangeThreshold) {

                //Ensure that the lane change will leave enough clearance between requester and new follower.
                if (prospectiveFollower != null) {

                    if (requester.getPosition() - prospectiveFollower.getPosition() - requester.getLength() >= Simulation.SimConstants.minJamDistance) {
                        //Change Lane to target lane
                        requester.changeLane(targetLane);
                    }
                    //Else there will not be enough clearance from the new follower after the lane change
                }
                else {
                    //No new follower. Change Lane to target lane
                    requester.changeLane(targetLane);
                }
            }
            //Else do nothing. Leave the vehicle in its current lane.
        }
    }
}
