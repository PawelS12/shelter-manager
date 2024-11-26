package com.example.shelterjavafx.test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AnimalConditionTest.class,
        AnimalShelterTest.class,
        AnimalTest.class,
        ShelterManagerTest.class,
        StudentTest.class
})
public class TestSuite {
}

