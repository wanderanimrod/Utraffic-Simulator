package com.iKairos.trafficSim.test.helpers;

import com.iKairos.utils.Numbers;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import static org.hamcrest.CoreMatchers.is;

public class Matchers {
    public static Matcher isRoughly(double valueToMatch) {
        return is(valueToMatch);
    }
    public static void assertThatResult(double roughResult, Matcher matcher) {
        MatcherAssert.assertThat(Numbers.round(roughResult, 3), matcher);
    }
    public static void assertMagnitudeOf(double roughSignedResult, Matcher matcher) {
        MatcherAssert.assertThat(Math.abs(Numbers.round(roughSignedResult, 3)), matcher);
    }
}
