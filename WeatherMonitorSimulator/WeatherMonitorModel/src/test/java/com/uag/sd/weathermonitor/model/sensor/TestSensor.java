package com.uag.sd.weathermonitor.model.sensor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uag.sd.weathermonitor.model.device.DeviceData;

@ContextConfiguration(locations = { "classpath:META-INF/spring/spring-ctx.xml" })
public class TestSensor extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("temperatureSensor")
	private Sensor tSensor;
	
	@Autowired
	@Qualifier("humiditySensor")
	private Sensor hSensor;
	
	private ExecutorService service;
	
	@BeforeClass
	public void init() {
		service = Executors.newFixedThreadPool(2);
		tSensor.setId("T1");
		tSensor.setActive(true);
		tSensor.setLapse(2000);
		tSensor.setMonitor(new SensorMonitor() {
			@Override
			public void notify(DeviceData data) {
				System.out.println(data.getData());
			}
		});
		
		hSensor.setId("H1");
		hSensor.setActive(true);
		hSensor.setLapse(2000);
		hSensor.setMonitor(new SensorMonitor() {
			@Override
			public void notify(DeviceData data) {
				System.out.println(data.getData());
			}
		});
	}
	
	@Test
	public void testSensors() throws InterruptedException {
		service.execute(tSensor);
		service.execute(hSensor);
		Thread.sleep(10000);
		tSensor.setActive(false);
		hSensor.setActive(false);
		Thread.sleep(3000);
	}
}
