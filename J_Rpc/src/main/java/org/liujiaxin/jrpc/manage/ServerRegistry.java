package org.liujiaxin.jrpc.manage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.liujiaxin.jrpc.core.InterfaceInfo;
import org.liujiaxin.jrpc.core.server.ServerInfo;
import org.liujiaxin.jrpc.util.Constant;
import org.apache.zookeeper.ZooKeeper;

public class ServerRegistry {


    private CountDownLatch latch = new CountDownLatch(1);

    private String serviceCenterAddr;

    public ServerRegistry(String serviceCenterAddr) {
        this.serviceCenterAddr = serviceCenterAddr;
    }

    public void register(ServerInfo serverInfo) {
        ZooKeeper zk = connectServiceCenter(serviceCenterAddr);
        if (zk != null) {
            createNode(zk, serverInfo);
        }

    }

    private ZooKeeper connectServiceCenter(String serviceCenterAddr) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(serviceCenterAddr, Constant.ZK_SESSION_TIMEOUT, new Watcher() {

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
            String path = Constant.ZK_DATA_PATH + "/" + i.getInterfaceName();

            String serverAddr = serverInfo.getServerAddr();
            try {
                if (zk.exists(path, true) == null) {
                    byte[] data = serverAddr.getBytes();
                    zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } else {

                    String s = new String(zk.getData(path, true, null));
                    s = s + ";" + serverAddr;
                    zk.setData(path, s.getBytes(), -1);

                    zk.setData(path, serverAddr.getBytes(), -1);
                }

            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
