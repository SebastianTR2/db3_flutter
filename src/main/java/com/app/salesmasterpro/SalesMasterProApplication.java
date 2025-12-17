package com.app.salesmasterpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SalesMasterProApplication
 * Main entry point for SalesMasterPro Backend
 * Compatible with Flutter app
 */
@SpringBootApplication
public class SalesMasterProApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesMasterProApplication.class, args);
        System.out.println(" SalesMasterPro Backend Started Successfully");
        System.out.println(" Compatible with Flutter App");
        System.out.println(" JWT Authentication Enabled");
    }
}
