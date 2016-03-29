package com.jason.JRpc.server;

import java.util.HashMap;
import java.util.Map;

public class RpcServer implements JRpcServer {

	private ServerRegistry serverRegistry;
	private ServerInfo serverInfo;
	
	//key为代表某一种版本的接口，value为该特定接口的实现类
	private HashMap<InterfaceInfo, Object> services;
	

	public HashMap<InterfaceInfo, Object> getServices() {
		return services;
	}

	public void setServices(HashMap<InterfaceInfo, Object> services) {
		this.services = services;
	}
	
	public RpcServer(ServerInfo serverInfo){
		this.serverInfo = serverInfo;
	}

	public void start() {
		

	}

}
