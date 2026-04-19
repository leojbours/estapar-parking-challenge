package com.estapar.challenge.parking;

import com.estapar.challenge.parking.config.ApplicationTestConfig;
import com.estapar.challenge.parking.service.impl.GarageInitializerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Import(ApplicationTestConfig.class)
class ParkingApplicationTests {

	@MockitoBean
	GarageInitializerService garageInitializerService;

	@Test
	void shouldLoadApplicationContext() {
	}

}
