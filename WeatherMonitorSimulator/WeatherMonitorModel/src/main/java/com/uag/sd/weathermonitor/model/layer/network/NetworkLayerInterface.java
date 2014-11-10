package com.uag.sd.weathermonitor.model.layer.network;

public interface NetworkLayerInterface {

	static final String NETWORK_LAYER_ADDRESS = "224.0.0.225";
	static final int NETWORK_LAYER_PORT = 6790;
	
	NetworkLayerResponse requestNetworkFormation(NetworlLayerRequest request);
	NetworkLayerResponse requestNetworkLayerNode(NetworlLayerRequest request);

}
