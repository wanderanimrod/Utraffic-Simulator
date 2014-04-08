package com.iKairos.trafficSim.test.helpers;

import com.iKairos.trafficSim.agents.Vehicle;
import com.iKairos.trafficSim.models.IDM;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

public class VehicleHelpers {

    IDM idmMock;

    public VehicleHelpers(IDM idmMock) {
        this.idmMock = idmMock;
    }

    public void fixIdmAcceleration(double acceleration) {
        when(this.idmMock.calculateAcceleration((Vehicle)anyObject(), (Vehicle)anyObject())).thenReturn(acceleration);
    }

    /** Makes the assumption that we use the second equation of motion in calculating displacement where t = 1*/
    public void moveVehicleToPosition(Vehicle vehicle, double desiredPosition) {
        double initialPosition = vehicle.getPosition();
        double initialVelocity = vehicle.getVelocity();
        double requiredAcceleration = 2 * (desiredPosition - initialPosition - initialVelocity);
        fixIdmAcceleration(requiredAcceleration);
        vehicle.translate(1);
    }
}
