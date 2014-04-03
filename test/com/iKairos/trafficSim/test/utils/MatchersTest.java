package com.iKairos.trafficSim.test.utils;

import org.junit.Test;

import static com.iKairos.trafficSim.test.helpers.Matchers.assertThatResult;
import static com.iKairos.trafficSim.test.helpers.Matchers.assertMagnitudeOf;
import static com.iKairos.trafficSim.test.helpers.Matchers.isRoughly;
import static org.hamcrest.CoreMatchers.not;

public class MatchersTest {
    @Test
    public void shouldRoughlyMatchNumbersWithAccuracyOf3DecimalPlaces() {
        assertThatResult(0.7335, isRoughly(0.734));
    }
    @Test
    public void shouldFailToMatchNumbersThatAreNotEqualTo3DecimalPlaces() {
        assertThatResult(0.7336, not(0.733));
    }
    @Test
    public void shouldMatchNumbersByMagnitudeDisregardingSigns() {
        assertMagnitudeOf(-0.0235, isRoughly(0.024));
    }
}