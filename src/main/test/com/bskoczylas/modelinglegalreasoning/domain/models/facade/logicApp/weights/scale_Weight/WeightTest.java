package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class WeightTest {
    @Test
    void testConstructorWithValidValue() {
        Scale scale = new Scale(1, 3);
        Weight weight = new Weight(scale, 2);
        assertEquals(2, weight.getValue());
    }

    @Test
    void testConstructorWithQuestionMark() {
        Scale scale = new Scale(1, 3);
        Weight weight = new Weight(scale, "?");
        assertEquals("?", weight.getValue());
    }

    @Test
    void testConstructorWithInvalidValue() {
        Scale scale = new Scale(1, 3);
        assertThrows(IllegalArgumentException.class, () -> new Weight(scale, 4));
    }
}