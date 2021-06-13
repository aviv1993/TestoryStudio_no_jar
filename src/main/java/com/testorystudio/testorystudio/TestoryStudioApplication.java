package com.testorystudio.testorystudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestoryStudioApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestoryStudioApplication.class, args);
    }

}
