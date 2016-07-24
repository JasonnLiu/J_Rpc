package org.liujiaxin.jrpc.core.server;

import java.util.List;

import org.liujiaxin.jrpc.core.InterfaceInfo;

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
