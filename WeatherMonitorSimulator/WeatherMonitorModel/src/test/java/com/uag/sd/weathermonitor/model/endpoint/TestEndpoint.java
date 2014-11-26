package com.uag.sd.weathermonitor.model.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
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
	private ZigBeeDevice coordinator;
	private ZigBeeDevice endpoint;
	
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
		DeviceLog coordinatorLog = new DefaultDeviceLog();
		DeviceLog endpointLog = new DefaultDeviceLog();
		coordinator = new ZigBeeDevice("ZigBee Device Coordinator", coordinatorLog, coordinatorLog);
		endpoint = new ZigBeeDevice("ZigBee Device Endpoint", endpointLog, endpointLog);
		//zigBeeDevice.addSensor(tSensor);
		//zigBeeDevice.addSensor(hSensor);
		//coordinator.setActive(true);
		coordinator.setCoordinator(true);
		coordinator.setAllowJoin(true);
		
		endpoint.setEndpoint(true);
		endpoint.setAllowJoin(false);
		//zigBeeRouter.setId("ZibBee Router");
		//zigBeeRouter.setActive(true);
	}
	
	@Test
	public void testEndpoint() throws InterruptedException {
		service.execute(coordinator);
		service.execute(endpoint);
		//service.execute(zigBeeRouter);
		Thread.sleep(3000);
		
		coordinator.networkFormation();
		Map<RFChannel, List<Beacon>> availableNetworksMap = endpoint.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				endpoint.networkJoin(channel, beacon);
				break;
			}
		}
		
		//zigBeeRouter.setCoordinator(true);
		//zigBeeRouter.establishNetwork();
		Thread.sleep(20000);
		coordinator.stop();
		endpoint.stop();
		//zigBeeRouter.stop();
		Thread.sleep(5000);
	}
}
