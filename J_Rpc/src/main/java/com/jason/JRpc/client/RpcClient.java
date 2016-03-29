package com.jason.JRpc.client;

import com.jason.JRpc.entity.RpcRequest;
import com.jason.JRpc.entity.RpcResponse;

public class RpcClient {

	private String host;
	private String port;

	private final Object obj = new Object();

	public RpcClient(String host, String port) {
		this.host = host;
		this.port = port;
	}
	public RpcResponse send(RpcRequest req){
		return null;
		
	}
}
