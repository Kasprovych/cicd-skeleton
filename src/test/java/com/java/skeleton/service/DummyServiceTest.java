package com.java.skeleton.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyServiceTest {

    @Test
    void add() {
        DummyService dummyService = new DummyService();
        int result = dummyService.add(2, 3);
        assertEquals(5, result);
    }


}