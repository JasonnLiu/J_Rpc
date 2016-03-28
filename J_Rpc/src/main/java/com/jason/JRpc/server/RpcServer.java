package com.jason.JRpc.server;

import java.util.HashMap;
import java.util.Map;

public class RpcServer implements JRpcServer {

	private HashMap<String, Object> services;

	public HashMap<String, Object> getServices() {
		return services;
	}

	public void setServices(HashMap<String, Object> services) {
		this.services = services;
	}

	public void start() {
		

	}

}
