package org.liujiaxin.jrpc.manage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.liujiaxin.jrpc.core.InterfaceInfo;
import org.liujiaxin.jrpc.util.Constant;

public class ServerDiscovery {

    private String serviceCenterAddr;

    public ServerDiscovery(String serviceCenterAddr) {
        this.serviceCenterAddr = serviceCenterAddr;
    }

    public String discover(InterfaceInfo interfaceInfo) {
        ZooKeeper zk = connectServiceCenter(serviceCenterAddr);
        String serverAddr = searchByInterfaceInfo(zk, interfaceInfo);
        return serverAddr;
    }

    private CountDownLatch latch = new CountDownLatch(1);

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

    private String searchByInterfaceInfo(ZooKeeper zk, InterfaceInfo interfaceInfo) {
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
        return s;
    }

}
