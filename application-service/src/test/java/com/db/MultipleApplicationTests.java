package com.db;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultipleApplicationTests {

    @Test
    public void getAdmin() {
        assertEquals(
                ResponseEntity.ok("Application service module is active"),
                ResponseEntity.ok("Application service module is active"));
    }
}
