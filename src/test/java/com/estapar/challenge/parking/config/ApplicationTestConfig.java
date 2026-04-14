package com.estapar.challenge.parking.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class ApplicationTestConfig {

  @Bean
  @ServiceConnection
  MySQLContainer mySqlContainer() {
    return new MySQLContainer("mysql:latest");
  }
}
