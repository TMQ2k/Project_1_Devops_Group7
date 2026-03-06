package com.yas.customer;

import com.yas.commonlibrary.config.CorsConfig;
import com.yas.customer.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.yas.customer", "com.yas.commonlibrary"})
@EnableConfigurationProperties({ServiceUrlConfig.class, CorsConfig.class})
public class CustomerApplication {

    // CI test: verify Jenkins changeset detection for customer service
    // CI test: verify Jenkins changeset detection for customer service - added a comment to trigger changeset detection
    // CI test: verify Jenkins changeset detection for customer service - added a comment to trigger changeset detection for coverage > 70%
    // CI trigger: 2026-03-06 - test pipeline coverage enforcement (expect FAIL when coverage < 70%)
    // CI trigger: 2026-03-06 - test pipeline coverage enforcement (expect FAIL when coverage < 70%) - added a comment to trigger changeset detection
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
