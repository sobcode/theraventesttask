package com.app.theraventesttask.usertests.integrationTests;

import com.app.theraventesttask.config.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtIT {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testTokenCreationAndValidation_thenTheyMatch() {
        String token = jwtTokenProvider.createToken("username", "admin");

        assertTrue(jwtTokenProvider.validateToken(token));
    }
}
