package com.jason.JRpc.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event;

import com.jason.JRpc.server.Constant;
import com.jason.JRpc.server.InterfaceInfo;
import com.jason.JRpc.server.ServerInfo;

public class ServerDiscovery {

	private String serviceCenterAddr;

	public ServerDiscovery(String s) {
		this.serviceCenterAddr = s;
	}

	public String discover(InterfaceInfo interfaceInfo) {
		if(interfaceInfo != null){
			ZooKeeper zk = connectServiceCenter(serviceCenterAddr);
			String serverAddr = searchByInterfaceInfo(zk, interfaceInfo);
			return serverAddr;
		}
		return null;
	}

	private CountDownLatch latch = new CountDownLatch(1);

	private ZooKeeper connectServiceCenter(String serviceCenterAddr) {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(serviceCenterAddr,
					Constant.ZK_SESSION_TIMEOUT, new Watcher() {

						public void process(WatchedEvent event) {
							if (event.getState() == Event.KeeperState.SyncConnected) {
								latch.countDown();
							}

						}
					});
			latch.await();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return zk;
	}
	private String searchByInterfaceInfo(ZooKeeper zk,InterfaceInfo interfaceInfo){
		String path = Constant.ZK_DATA_PATH + "/" + interfaceInfo.getInterfaceName();
		String addr = null;
		try {
			byte[] b = zk.getData(path, false, null);
			addr = new String(b);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String[] addrs = addr.split(";");
		String s = addrs[ThreadLocalRandom.current().nextInt(addrs.length)];
		return s ;
	}
	
	
}
