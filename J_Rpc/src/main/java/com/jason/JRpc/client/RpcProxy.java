package com.jason.JRpc.client;

import com.jason.JRpc.server.InterfaceInfo;

public class RpcProxy {
	private InterfaceInfo interfaceInfo;
	private ServerDiscovery serverDiscovery;
	
	public RpcProxy(InterfaceInfo interfaceInfo,ServerDiscovery serverDiscovery){
		this.interfaceInfo = interfaceInfo;
		this.serverDiscovery = serverDiscovery;
	}

}
