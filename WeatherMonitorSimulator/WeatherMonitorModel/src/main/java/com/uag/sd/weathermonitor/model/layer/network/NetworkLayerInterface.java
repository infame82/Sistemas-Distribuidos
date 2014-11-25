package com.uag.sd.weathermonitor.model.layer.network;

public interface NetworkLayerInterface {

	static final String NETWORK_LAYER_ADDRESS = "224.0.0.225";
	static final int NETWORK_LAYER_PORT = 6790;
	static final int MAX_REQUEST = 5;
	static final int REQUEST_TIME_OUT = 1000;
	static final int BUFFER_SIZE = 1024;
	
	
	
	NetworkLayerResponse requestNetworkFormation(NetworlLayerRequest request);
	NetworkLayerResponse requestNetworkLayerNode(NetworlLayerRequest request);
	NetworkLayerResponse networkDiscovery(NetworlLayerRequest request);
	
	NetworkLayerResponse requestExtenedPanId(NetworlLayerRequest request);

}
