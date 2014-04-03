package com.iKairos.trafficSim.test.utils;

import org.junit.Test;

import static com.iKairos.trafficSim.test.helpers.Helpers.assertResult;
import static com.iKairos.trafficSim.test.helpers.Helpers.isRoughly;
import static org.hamcrest.CoreMatchers.not;

public class HelpersTest {
    @Test
    public void shouldRoughlyMatchNumbersWithAccuracyOf3DecimalPlaces() {
        assertResult(0.7335, isRoughly(0.734));
    }
    @Test
    public void shouldFailToMatchNumbersThatAreNotEqualTo3DecimalPlaces() {
        assertResult(0.7336, not(0.733));
    }
}