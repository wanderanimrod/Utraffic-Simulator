package com.iKairos.trafficSim.test.helpers;

import com.iKairos.utils.Numbers;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Matchers {
    public static Matcher isRoughly(double valueToMatch) {
        return is(valueToMatch);
    }
    public static void assertThatResult(double roughResult, Matcher matcher) {
        assertThat(Numbers.round(roughResult, 3), matcher);
    }
    public static void assertThatMagnitudeOf(double roughSignedResult, Matcher matcher) {
        double roundedUnsignedResult = Math.abs(Numbers.round(roughSignedResult, 3));
        assertThat(roundedUnsignedResult, matcher);
    }
}