package com.uag.sd.weathermonitor.gui;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;

public interface EndpointRefresher {

	void refreshEndpointDetails(Endpoint endpoint);
}
