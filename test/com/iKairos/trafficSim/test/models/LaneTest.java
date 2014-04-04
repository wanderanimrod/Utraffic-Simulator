package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.network.Lane;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LaneTest {
    @Test
    public void shouldReturnLeaderAsAVehicleFarAwayWhenRequesterIsTheLeadingVehicleOnTheLane() {
        Lane lane = new Lane(1);
        Car requester = new Car(1);
        lane.insertVehicleAtTheStartOfTheLane(requester);
        Vehicle leader = lane.getLeadingVehicle(requester);
        assertThat(leader, equalTo((Vehicle)new Car(-1)));
        assertThat(leader.getPosition(), is(100000d));
    }
}
