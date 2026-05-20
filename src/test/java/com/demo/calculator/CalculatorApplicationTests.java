package com.demo.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CalculatorApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void testAdd() {
        String result = restTemplate.getForObject("/add?a=3&b=4", String.class);
        assertThat(result).isEqualTo("7");
    }

    @Test
    void testSubtract() {
        String result = restTemplate.getForObject("/subtract?a=10&b=3", String.class);
        assertThat(result).isEqualTo("7");
    }

    @Test
    void testMultiply() {
        String result = restTemplate.getForObject("/multiply?a=3&b=4", String.class);
        assertThat(result).isEqualTo("12");
    }

    @Test
    void testDivide() {
        String result = restTemplate.getForObject("/divide?a=10&b=2", String.class);
        assertThat(result).isEqualTo("5");
    }

    @Test
    void testDivideByZero() {
        String result = restTemplate.getForObject("/divide?a=10&b=0", String.class);
        assertThat(result).contains("500");
    }
}
