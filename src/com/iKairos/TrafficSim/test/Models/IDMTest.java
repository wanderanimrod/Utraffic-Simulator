package com.iKairos.trafficSim.test.models;

import com.iKairos.trafficSim.agents.Car;
import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.Constants;
import com.iKairos.trafficSim.models.IDM;
import com.iKairos.utils.Numbers;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class IDMTest {
    
    @Test
    public void shouldReturnAcceleration0_730ForLeadingVehicle() {

        Vehicle vehicle = new Car(0);
        Constants.dummyLeadingVehicle = new Car(-1);
        Constants.dummyLeadingVehicle.setPosition(100000.0d);
        Constants.dummyLeadingVehicle.setAcceleration(0.0d);
        Constants.dummyLeadingVehicle.setVelocity(27.78d);

        IDM idm = new IDM();
        double acceleration = idm.calculateAcceleration(Constants.dummyLeadingVehicle, vehicle);

        assertEquals(0.730, Numbers.round(acceleration, 3));
    }
}
