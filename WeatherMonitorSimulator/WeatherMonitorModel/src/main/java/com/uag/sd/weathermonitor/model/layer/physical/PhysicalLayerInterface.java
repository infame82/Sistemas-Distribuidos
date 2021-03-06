package com.uag.sd.weathermonitor.model.layer.physical;



public interface PhysicalLayerInterface {

	static final String PHYSICAL_LAYER_ADDRESS = "224.0.0.225";
	static final int PHYSICAL_LAYER_PORT = 6792;
	static final int MAX_REQUEST = 3;
	static final int REQUEST_TIME_OUT = 5000;
	static final int BUFFER_SIZE = 5096;
	
	PhysicalLayerResponse requestPhysicalLayerNode(PhysicalLayerRequest request);
	
	PhysicalLayerResponse getChannels(PhysicalLayerRequest request);
	
	PhysicalLayerResponse increaseEnergyLevel(PhysicalLayerRequest request);
}
