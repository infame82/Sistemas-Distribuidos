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

import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.device.DefaultDeviceLog;
import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;
import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.device.ZigBeeRouter;
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
	
	private ZigBeeEndpoint e01;
	private ZigBeeEndpoint e02;
	private ZigBeeEndpoint e03;
	private ZigBeeEndpoint e04;
	
	private ZigBeeEndpoint e05;
	private ZigBeeEndpoint e06;
	private ZigBeeEndpoint e07;
	private ZigBeeEndpoint e08;
	
	private ZigBeeEndpoint e09;
	private ZigBeeEndpoint e10;
	private ZigBeeEndpoint e11;
	private ZigBeeEndpoint e12;
	
	private ZigBeeEndpoint e13;
	private ZigBeeEndpoint e14;
	private ZigBeeEndpoint e15;
	private ZigBeeEndpoint e16;
	
	
	
	private ZigBeeRouter r1;
	private ZigBeeRouter r2;
	private ZigBeeRouter r3;
	private ZigBeeRouter r4;
	
	
	//@Autowired
	//@Qualifier("zigBeeRouter")
	//private ZigBeeRouter zigBeeRouter;
	
	private ExecutorService service;
	
	@BeforeClass
	public void init() throws IOException {
		service = Executors.newFixedThreadPool(50);

		
	}
	
	public static boolean isCovered(Point a,int pA,Point b,int pB) {
		
		return false;
	}
	
	@Test
	public void testEndpoint() throws InterruptedException, IOException {
		
		c1 = new ZigBeeCoordinator("Coordinator 00",  new DefaultDeviceLog(),"localhost");
		c1.setLocation(9, 2);
		c1.setPotency(3);
		
		
		service.execute(c1);		
		c1.networkFormation();

		Beacon network = null;
		RFChannel networkChannel = null;
		
		r2 = new ZigBeeRouter("Router 02",  new DefaultDeviceLog());
		r2.setLocation(5,2);
		r2.setPotency(2);
		service.execute(r2);
		Map<RFChannel, List<Beacon>> availableNetworksMap = r2.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				networkChannel = channel;
				network = beacons.get(0);
				break;
			}
		}
		r2.networkJoin(networkChannel, network);
		
		r1 = new ZigBeeRouter("Router 01",  new DefaultDeviceLog());
		r1.setLocation(5,6);
		r1.setPotency(2);
		service.execute(r1);
		availableNetworksMap = r1.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				networkChannel = channel;
				network = beacons.get(0);
				break;
			}
		}
		r1.networkJoin(networkChannel, network);

		r4 = new ZigBeeRouter("Router 04",  new DefaultDeviceLog());
		r4.setLocation(2,2);
		r4.setPotency(2);
		service.execute(r4);
		availableNetworksMap = r4.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				networkChannel = channel;
				network = beacons.get(0);
				break;
			}
		}
		r4.networkJoin(networkChannel, network);
		
		r3 = new ZigBeeRouter("Router 03",  new DefaultDeviceLog());
		r3.setLocation(2,6);
		r3.setPotency(2);
		
		
		service.execute(r3);
		availableNetworksMap = r3.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				networkChannel = channel;
				network = beacons.get(0);
				break;
			}
		}
		r3.networkJoin(networkChannel, network);
		
		e01 = new ZigBeeEndpoint("Endpoint 01", new DefaultDeviceLog(), new DefaultDeviceLog());
		e01.setLocation(1,1);
		e01.setPotency(1);
		e01.addSensor(new TemperatureSensor("T1","5"));
		
		availableNetworksMap = e01.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e01.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e01);
		
		e02 = new ZigBeeEndpoint("Endpoint 02", new DefaultDeviceLog(), new DefaultDeviceLog());
		e02.setLocation(1,3);
		e02.setPotency(1);
		e02.addSensor(new TemperatureSensor("T2","5"));
		availableNetworksMap = e02.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e02.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e02);

		e03 = new ZigBeeEndpoint("Endpoint 03", new DefaultDeviceLog(), new DefaultDeviceLog());
		e03.setLocation(1,5);
		e03.setPotency(1);
		e03.addSensor(new TemperatureSensor("T3","5"));
		availableNetworksMap = e03.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e03.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e03);
		
		e04 = new ZigBeeEndpoint("Endpoint 04", new DefaultDeviceLog(), new DefaultDeviceLog());
		e04.setLocation(1,7);
		e04.setPotency(1);
		e04.addSensor(new TemperatureSensor("T4","5"));
		availableNetworksMap = e04.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e04.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e04);
		
		
		e05 = new ZigBeeEndpoint("Endpoint 05", new DefaultDeviceLog(), new DefaultDeviceLog());
		e05.setLocation(3,1);
		e05.setPotency(1);
		e05.addSensor(new TemperatureSensor("T5","5"));
		availableNetworksMap = e05.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e05.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e05);
		
		e06 = new ZigBeeEndpoint("Endpoint 06", new DefaultDeviceLog(), new DefaultDeviceLog());
		e06.setLocation(3,3);
		e06.setPotency(1);
		e06.addSensor(new TemperatureSensor("T6","5"));
		availableNetworksMap = e06.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e06.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e06);
		
		e07 = new ZigBeeEndpoint("Endpoint 07", new DefaultDeviceLog(), new DefaultDeviceLog());
		e07.setLocation(3,5);
		e07.setPotency(1);
		e07.addSensor(new TemperatureSensor("T7","5"));
		availableNetworksMap = e07.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e07.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e07);
		
		e08 = new ZigBeeEndpoint("Endpoint 08", new DefaultDeviceLog(), new DefaultDeviceLog());
		e08.setLocation(3,7);
		e08.setPotency(1);
		e08.addSensor(new TemperatureSensor("T8","5"));
		availableNetworksMap = e08.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e08.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e08);
		
		e09 = new ZigBeeEndpoint("Endpoint 09", new DefaultDeviceLog(), new DefaultDeviceLog());
		e09.setLocation(5,1);
		e09.setPotency(1);
		e09.addSensor(new TemperatureSensor("T09","5"));
		availableNetworksMap = e09.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e09.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e09);
		
		e10 = new ZigBeeEndpoint("Endpoint 10", new DefaultDeviceLog(), new DefaultDeviceLog());
		e10.setLocation(5,3);
		e10.setPotency(1);
		e10.addSensor(new TemperatureSensor("T10","2"));
		availableNetworksMap = e10.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e10.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e10);
		
		e11 = new ZigBeeEndpoint("Endpoint 11", new DefaultDeviceLog(), new DefaultDeviceLog());
		e11.setLocation(5,5);
		e11.setPotency(1);
		e11.addSensor(new TemperatureSensor("T11","2"));
		availableNetworksMap = e11.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e11.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e11);
		
		e12 = new ZigBeeEndpoint("Endpoint 12", new DefaultDeviceLog(), new DefaultDeviceLog());
		e12.setLocation(5,7);
		e12.setPotency(1);
		e12.addSensor(new TemperatureSensor("T12","2"));
		availableNetworksMap = e12.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e12.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e12);
		
		e13 = new ZigBeeEndpoint("Endpoint 13", new DefaultDeviceLog(), new DefaultDeviceLog());
		e13.setLocation(7,1);
		e13.setPotency(1);
		e13.addSensor(new TemperatureSensor("T13","22"));
		availableNetworksMap = e13.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e13.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e13);
		
		e14 = new ZigBeeEndpoint("Endpoint 14", new DefaultDeviceLog(), new DefaultDeviceLog());
		e14.setLocation(7,3);
		e14.setPotency(1);
		e14.addSensor(new TemperatureSensor("T14","23"));
		availableNetworksMap = e14.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e14.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e14);
		
		e15 = new ZigBeeEndpoint("Endpoint 15", new DefaultDeviceLog(), new DefaultDeviceLog());
		e15.setLocation(7,5);
		e15.setPotency(1);
		e15.addSensor(new TemperatureSensor("T15","24"));
		availableNetworksMap = e15.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e15.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e15);
		
		e16 = new ZigBeeEndpoint("Endpoint 16", new DefaultDeviceLog(), new DefaultDeviceLog());
		e16.setLocation(7,7);
		e16.setPotency(1);
		e16.addSensor(new TemperatureSensor("T16","25"));
		availableNetworksMap = e16.networkDiscovery();
		for(RFChannel channel:availableNetworksMap.keySet()) {
			List<Beacon> beacons = availableNetworksMap.get(channel);
			if(beacons!=null && !beacons.isEmpty()) {
				Beacon beacon = beacons.get(0);
				e16.networkJoin(channel, beacon);
				break;
			}
		}
		service.execute(e16);
		
		
		
		
		//zigBeeRouter.setCoordinator(true);
		//zigBeeRouter.establishNetwork();
		Thread.sleep(20000);
		c1.stop();
		e13.stop();
		//zigBeeRouter.stop();
		Thread.sleep(5000);
	}
}
