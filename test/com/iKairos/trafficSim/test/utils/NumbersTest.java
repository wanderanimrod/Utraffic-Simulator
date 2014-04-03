package com.iKairos.trafficSim.test.utils;

import com.iKairos.utils.Numbers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumbersTest {
    @Test
    public void shouldRoundOffNumberByRaisingLastDigitWhenTrailingDecimalIsGreaterThan5() {
        double rounded = Numbers.round(0.7336, 3);
        assertThat(rounded, is(0.734));
    }
    @Test
    public void shouldRoundOfNumberByTruncatingWhenTrailingDecimalIsLessThan5() {
        double rounded = Numbers.round(0.7334, 3);
        assertThat(rounded, is(0.733));
    }
    @Test
    public void shouldRoundOfNumberByRaisingLastDigitWhenTrailedBy5() {
        double rounded = Numbers.round(0.7335, 3);
        assertThat(rounded, is(0.734));
    }
    @Test
    public void BUGDoesNotRound2UpTo3WhenTrailedBy5() {
        double rounded = Numbers.round(0.7325, 3);
        assertThat(rounded, is(0.732));
    }
}