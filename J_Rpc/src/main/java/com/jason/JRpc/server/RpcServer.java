package com.jason.JRpc.server;

import java.util.HashMap;
import java.util.Map;

public class RpcServer implements JRpcServer {

	private ServerRegistry serverRegistry;
	private ServerInfo serverInfo;
	
	//keyΪ����ĳһ�ְ汾�Ľӿڣ�valueΪ���ض��ӿڵ�ʵ����
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
