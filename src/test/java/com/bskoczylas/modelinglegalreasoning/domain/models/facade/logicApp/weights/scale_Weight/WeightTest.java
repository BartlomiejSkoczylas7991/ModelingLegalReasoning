package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class WeightTest {
    @Test
    void testConstructorWithValidValue() {
        Scale scale = new Scale(1, 3);
        Weight weight = Weight.of(2);
        assertEquals(2, weight.getWeight());
    }

    @Test
    void testConstructorWithQuestionMark() {
        Scale scale = new Scale(1, 3);
        Weight weight = Weight.indeterminate();
        assertEquals(null, weight.getWeight());
    }

}