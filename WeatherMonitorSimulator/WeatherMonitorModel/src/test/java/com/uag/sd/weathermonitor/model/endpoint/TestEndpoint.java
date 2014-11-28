package com.uag.sd.weathermonitor.model.endpoint;

import java.awt.Point;
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
import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;
import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

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
	private ZigBeeCoordinator c1;
	private ZigBeeEndpoint e13;
	private ZigBeeEndpoint e14;
	private ZigBeeEndpoint e15;
	private ZigBeeEndpoint e16;
	
	
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
		c1 = new ZigBeeCoordinator("Coordinator 00", coordinatorLog);
		c1.setLocation(9, 2);
		c1.setPotency(3);
		
		e13 = new ZigBeeEndpoint("Endpoint 13", endpointLog, endpointLog);
		e13.setLocation(7,1);
		e13.setPotency(1);
		e13.addSensor(new TemperatureSensor("T13","22"));
		
		e14 = new ZigBeeEndpoint("Endpoint 14", endpointLog, endpointLog);
		e14.setLocation(7,3);
		e14.setPotency(1);
		e14.addSensor(new TemperatureSensor("T14","23"));
		
		e15 = new ZigBeeEndpoint("Endpoint 15", endpointLog, endpointLog);
		e15.setLocation(7,5);
		e15.setPotency(1);
		e15.addSensor(new TemperatureSensor("T15","24"));
		
		e16 = new ZigBeeEndpoint("Endpoint 16", endpointLog, endpointLog);
		e16.setLocation(7,7);
		e16.setPotency(1);
		e16.addSensor(new TemperatureSensor("T16","25"));
	}
	
	public static boolean isCovered(Point a,int pA,Point b,int pB) {
		
		return false;
	}
	
	@Test
	public void testEndpoint() throws InterruptedException {
		service.execute(c1);
		service.execute(e13);
		service.execute(e14);
		service.execute(e15);
		service.execute(e16);
		//service.execute(zigBeeRouter);
		Thread.sleep(3000);
		
		c1.networkFormation();
		
		Map<RFChannel, List<Beacon>> availableNetworksMap = e13.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e13.networkJoin(channel, beacon);
				break;
			}
		}
		
		availableNetworksMap = e14.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e14.networkJoin(channel, beacon);
				break;
			}
		}
		
		availableNetworksMap = e15.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e15.networkJoin(channel, beacon);
				break;
			}
		}
		
		availableNetworksMap = e16.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e16.networkJoin(channel, beacon);
				break;
			}
		}
		
		
		
		//zigBeeRouter.setCoordinator(true);
		//zigBeeRouter.establishNetwork();
		Thread.sleep(20000);
		c1.stop();
		e13.stop();
		//zigBeeRouter.stop();
		Thread.sleep(5000);
	}
}
