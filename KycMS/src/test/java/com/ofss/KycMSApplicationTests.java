package com.ofss;

import com.ofss.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class KycMSApplicationTests {

    // Mock beans that depend on external services
    @MockBean
    private EmailService emailService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Test just checks that Spring context starts
    }
}
