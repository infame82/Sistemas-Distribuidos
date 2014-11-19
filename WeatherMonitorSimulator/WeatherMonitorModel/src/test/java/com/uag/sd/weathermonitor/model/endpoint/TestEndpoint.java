package com.uag.sd.weathermonitor.model.endpoint;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uag.sd.weathermonitor.model.device.DefaultDeviceLog;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
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
	
	
	//@Autowired
	//@Qualifier("zigBeeDevice")
	private ZigBeeDevice zigBeeDevice;
	
	//@Autowired
	//@Qualifier("zigBeeRouter")
	//private ZigBeeRouter zigBeeRouter;
	
	private ExecutorService service;
	
	@BeforeClass
	public void init() throws IOException {
		service = Executors.newFixedThreadPool(5);
		tSensor.setLapse(2000);
		tSensor.setId("T1");
		hSensor.setLapse(2000);
		hSensor.setId("H1");
		DeviceLog log = new DefaultDeviceLog();
		zigBeeDevice = new ZigBeeDevice("ZigBee Device", log, log);
		zigBeeDevice.addSensor(tSensor);
		zigBeeDevice.addSensor(hSensor);
		zigBeeDevice.setActive(true);
		
		//zigBeeRouter.setId("ZibBee Router");
		//zigBeeRouter.setActive(true);
	}
	
	@Test
	public void testEndpoint() throws InterruptedException {
		service.execute(zigBeeDevice);
		//service.execute(zigBeeRouter);
		Thread.sleep(3000);
		zigBeeDevice.setCoordinator(true);
		zigBeeDevice.establishNetwork();
		//zigBeeRouter.setCoordinator(true);
		//zigBeeRouter.establishNetwork();
		Thread.sleep(20000);
		zigBeeDevice.stop();
		//zigBeeRouter.stop();
		Thread.sleep(5000);
	}
}
