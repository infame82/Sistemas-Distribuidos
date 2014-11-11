package com.uag.sd.weathermonitor.model.layer.mac;

public interface MacLayerInterface {

	static final String MAC_LAYER_ADDRESS = "224.0.0.225";
	static final int MAC_LAYER_PORT = 6791;
	static final int MAX_REQUEST = 5;
	static final int REQUEST_TIME_OUT = 5000;
	static final int BUFFER_SIZE = 1024;
	
	MacLayerResponse requestMacLayerNode(MacLayerRequest request);
	MacLayerResponse energyDetectionScan(MacLayerRequest request);
	MacLayerResponse activeScan(MacLayerRequest request);
	MacLayerResponse setPANId(MacLayerRequest request);
	MacLayerResponse start(MacLayerRequest request);
}
