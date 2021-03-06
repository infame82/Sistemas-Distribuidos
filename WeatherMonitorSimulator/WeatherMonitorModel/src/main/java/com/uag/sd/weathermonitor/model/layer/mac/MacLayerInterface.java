package com.uag.sd.weathermonitor.model.layer.mac;

public interface MacLayerInterface {

	static final String MAC_LAYER_ADDRESS = "224.0.0.225";
	static final int MAC_LAYER_PORT = 6791;
	static final int MAX_REQUEST = 3;
	static final int REQUEST_TIME_OUT = 5000;
	static final int BUFFER_SIZE = 4096;
	static final int ACCEPTABLE_ENERGY_LEVEL = 10;
	
	MacLayerResponse requestMacLayerNode(MacLayerRequest request);
	MacLayerResponse energyDetectionScan(MacLayerRequest request);
	MacLayerResponse activeScan(MacLayerRequest request);
	MacLayerResponse setPANId(MacLayerRequest request);
	MacLayerResponse association(MacLayerRequest request);
	MacLayerResponse transmission(MacLayerRequest request);
	MacLayerResponse start(MacLayerRequest request);
	
	MacLayerResponse getRegisteredNetworks(MacLayerRequest request);
	MacLayerResponse getRegisteredDevices(MacLayerRequest request);
	MacLayerResponse registerDevice(MacLayerRequest request);
	MacLayerResponse getExtendedAddress(MacLayerRequest request);
	
}
