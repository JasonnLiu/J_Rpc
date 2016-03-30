package com.jason.JRpc.server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRegistry {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerRegistry.class);

	private CountDownLatch latch = new CountDownLatch(1);
	
	private String serviceCenterAddr;
	
	public ServerRegistry(String serviceCenterAddr ){
		this.serviceCenterAddr = serviceCenterAddr;
	}
	

	public void register(ServerInfo serverInfo) {
		if (serverInfo != null) {
			ZooKeeper zk = connectServiceCenter(serviceCenterAddr);
			if (zk != null) {
				createNode(zk, serverInfo);
			}
		} else {
			LOGGER.debug("serverInfo is null");
		}

	}

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

	private void createNode(ZooKeeper zk, ServerInfo serverInfo) {
		List<InterfaceInfo> l = serverInfo.getInterfaceInfo();

		for (InterfaceInfo i : l) {
			// path还可以根据interface的属性增加
			String path = Constant.ZK_DATA_PATH + "/" + i.getInterfaceName();

			String serverAddr = serverInfo.getServerAddr();
			try {
				if (zk.exists(path, true) == null) {
					byte[] data = serverAddr.getBytes();
					zk.create(path, data, Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				} else {
					/*
					String s = new String(zk.getData(path, true, null));
					LOGGER.debug(s);
					s = s+";"+serverAddr;
					LOGGER.debug(s);
					zk.setData(path, s.getBytes(), -1);
					*/
					zk.setData(path, serverAddr.getBytes(), -1);
					LOGGER.info(serverAddr);
				}

			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
