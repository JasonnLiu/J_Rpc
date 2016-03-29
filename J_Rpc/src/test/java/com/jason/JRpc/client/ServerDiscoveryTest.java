package com.jason.JRpc.client;

import org.junit.Test;

import com.jason.JRpc.server.InterfaceInfo;

public class ServerDiscoveryTest {

	@Test
	public void test() {
		String addr = "localhost:2181";
		ServerDiscovery sd = new ServerDiscovery(addr);
		
		InterfaceInfo i = new InterfaceInfo();
		i.setInterfaceName("HelloService");
		
		String s = sd.discover(i);
		System.out.println(s);
	}

}
