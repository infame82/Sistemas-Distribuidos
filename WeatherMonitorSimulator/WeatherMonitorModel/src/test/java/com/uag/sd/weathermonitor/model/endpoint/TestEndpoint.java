package com.uag.sd.weathermonitor.model.endpoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uag.sd.weathermonitor.model.router.ZigBeeRouter;
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
	@Qualifier("zigBeeDevice")
	private ZigBeeDevice zigBeeDevice;
	
	@Autowired
	@Qualifier("zigBeeRouter")
	private ZigBeeRouter zigBeeRouter;
	
	private ExecutorService service;
	
	@BeforeClass
	public void init() {
		service = Executors.newFixedThreadPool(5);
		tSensor.setLapse(2000);
		tSensor.setId("T1");
		hSensor.setLapse(2000);
		hSensor.setId("H1");
		zigBeeDevice.setId("ZigBee Device");
		zigBeeDevice.addSensor(tSensor);
		zigBeeDevice.addSensor(hSensor);
		zigBeeDevice.setActive(true);
		
		zigBeeRouter.setId("ZibBee Router");
		zigBeeRouter.setActive(true);
	}
	
	@Test
	public void testEndpoint() throws InterruptedException {
		service.execute(zigBeeDevice);
		service.execute(zigBeeRouter);
		Thread.sleep(3000);
		zigBeeDevice.establishNetwork();
		//zigBeeRouter.establishNetwork();
		Thread.sleep(10000);
		zigBeeDevice.stop();
		zigBeeRouter.stop();
		Thread.sleep(3000);
	}
}
