package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
    @Test
    public void testConstructors() {
        Value value = new Value("Value Name");
        assertEquals("Value Name", value.getName());
        assertNotNull(value.getCreated());
    }

    @Test
    public void testGettersAndSetters() {
        Value value = new Value("Original Name");
        value.setName("New Name");
        value.setCreated(LocalDateTime.of(2023, 8, 14, 0, 0));

        assertEquals("New Name", value.getName());
        assertEquals(LocalDateTime.of(2023, 8, 14, 0, 0), value.getCreated());
    }

    @Test
    public void testFormattedCreated() {
        LocalDateTime created = LocalDateTime.of(2023, 8, 14, 10, 30);
        Value agent = new Value("Value Name");
        agent.setCreated(created);

        assertEquals("2023-08-14 10:30:00", agent.getFormattedCreated());
    }

    @Test
    public void testEqualsAndHashCode() {
        Value value1 = new Value("Value Name");
        Value value2 = new Value("Value Name");
        Value value3 = new Value("Different Name");

        assertEquals(value1, value2);
        assertNotEquals(value1, value3);
        assertEquals(value1.hashCode(), value2.hashCode());
        assertNotEquals(value1.hashCode(), value3.hashCode());
    }

    @Test
    public void testToString() {
        Value value = new Value("Value Name");
        assertEquals("Value Name", value.toString());
    }
}