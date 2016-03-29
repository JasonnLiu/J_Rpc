package com.jason.JRpc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ServerRegistryTest {

	@Test
	public void test() {
		String addr = "localhost:2181";
		
		ServerRegistry sr = new ServerRegistry(addr);
		
		ServerInfo si = new ServerInfo();
		
		InterfaceInfo ii = new InterfaceInfo();
		List<String> adress = new ArrayList<String>();
		ii.setInterfaceName("HelloService");
		
		List<InterfaceInfo> l = new ArrayList<InterfaceInfo>();
		l.add(ii);
		
		si.setInterfaceInfo(l);
		si.setServerAddr("localhost:2182");
		
		sr.register(si); 
		
		
		
	}

}
