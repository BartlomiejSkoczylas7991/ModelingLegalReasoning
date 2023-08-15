package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;
import org.junit.jupiter.api.Test;

class ScaleTest {
    @Test
    void testConstructorWithMinMaxValues() {
        Scale scale = new Scale(5, 10);
        assertEquals(5, scale.getMin());
        assertEquals(10, scale.getMax());
    }

    @Test
    void testDefaultConstructor() {
        Scale scale = new Scale();
        assertEquals(0, scale.getMin());
        assertEquals(10, scale.getMax());
    }

    @Test
    void testContainsWithValueInRange() {
        Scale scale = new Scale(5, 10);
        assertTrue(scale.contains(7));
    }

    @Test
    void testContainsWithValueOutOfRange() {
        Scale scale = new Scale(5, 10);
        assertFalse(scale.contains(4));
        assertFalse(scale.contains(11));
    }

    @Test
    void testSetScale() {
        Scale scale = new Scale(5, 10);
        scale.setScale(3, 8);
        assertEquals(3, scale.getMin());
        assertEquals(8, scale.getMax());
    }

    @Test
    void testSetScaleAndNotifyObservers() {
        Scale scale = new Scale(5, 10);
        ScaleObserver observer = mock(ScaleObserver.class);
        scale.addObserver(observer);
        scale.setScale(3, 8);
        verify(observer).updateScale(scale);
    }

    @Test
    void testEqualsAndHashCode() {
        Scale scale1 = new Scale(5, 10);
        Scale scale2 = new Scale(5, 10);
        assertEquals(scale1, scale2);
        assertEquals(scale1.hashCode(), scale2.hashCode());
    }
}