package com.iKairos.trafficSim.test.helpers;

import com.iKairos.utils.Numbers;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Helpers {
    public static Matcher isRoughly(double valueToMatch) {
        return is(valueToMatch);
    }
    public static void assertResult(double roughResult, Matcher matcher) {
        System.out.println("Rounded: " + Numbers.round(roughResult, 3));
        assertThat(Numbers.round(roughResult, 3), matcher);
    }
}
