package com.example.shelterjavafx.test;

import com.example.shelterjavafx.model.AnimalCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalConditionTest {

    @Test
    void testEnumValues() {
        AnimalCondition[] conditions = AnimalCondition.values();
        assertEquals(4, conditions.length);
        assertTrue(contains(conditions, AnimalCondition.HEALTHY));
        assertTrue(contains(conditions, AnimalCondition.SICK));
        assertTrue(contains(conditions, AnimalCondition.ADOPTION));
        assertTrue(contains(conditions, AnimalCondition.QUARANTINE));
    }

    @Test
    void testEnumValueOf() {
        assertEquals(AnimalCondition.HEALTHY, AnimalCondition.valueOf("HEALTHY"));
        assertEquals(AnimalCondition.SICK, AnimalCondition.valueOf("SICK"));
        assertEquals(AnimalCondition.ADOPTION, AnimalCondition.valueOf("ADOPTION"));
        assertEquals(AnimalCondition.QUARANTINE, AnimalCondition.valueOf("QUARANTINE"));
    }

    @Test
    void testEnumUniqueness() {
        AnimalCondition condition1 = AnimalCondition.HEALTHY;
        AnimalCondition condition2 = AnimalCondition.SICK;

        assertNotSame(condition1, condition2);
    }

    private boolean contains(AnimalCondition[] conditions, AnimalCondition condition) {
        for (AnimalCondition c : conditions) {
            if (c == condition) {
                return true;
            }
        }
        return false;
    }
}