package com.jason.JRpc.server;

import java.util.List;

public class ServerInfo {

	private String serverAddr;
	private List<InterfaceInfo> interfaceInfo;

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public List<InterfaceInfo> getInterfaceInfo() {
		return interfaceInfo;
	}

	public void setInterfaceInfo(List<InterfaceInfo> interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}

}
