package com.uag.sd.weathermonitor.model.endpoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uag.sd.weathermonitor.model.sensor.Sensor;

@ContextConfiguration(locations = { "classpath:META-INF/spring/spring-ctx.xml" })
public class TestEndpoint  extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("temperatureSensor")
	private Sensor tSensor;
	
	@Autowired
	@Qualifier("humiditySensor")
	private Sensor hSensor;
	
	
	@Autowired
	@Qualifier("endpoint")
	private Endpoint endpoint;
	
	private ExecutorService service;
	
	@BeforeClass
	public void init() {
		service = Executors.newFixedThreadPool(2);
		tSensor.setLapse(2000);
		hSensor.setLapse(2000);
		endpoint.setId("testEndpoint");
		endpoint.addSensor(tSensor);
		endpoint.addSensor(hSensor);
		endpoint.setActive(true);
	}
	
	@Test
	public void testEndpoint() throws InterruptedException {
		service.execute(endpoint);
		Thread.sleep(10000);
		endpoint.stop();
		Thread.sleep(3000);
	}
}
