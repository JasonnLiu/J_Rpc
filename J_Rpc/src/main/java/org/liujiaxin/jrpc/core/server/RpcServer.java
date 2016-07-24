package org.liujiaxin.jrpc.core.server;

import java.util.HashMap;
import java.util.Map;

import org.liujiaxin.jrpc.annotation.RpcService;
import org.liujiaxin.jrpc.core.InterfaceInfo;
import org.liujiaxin.jrpc.core.RpcHandler;
import org.liujiaxin.jrpc.entity.RpcRequest;
import org.liujiaxin.jrpc.entity.RpcResponse;
import org.liujiaxin.jrpc.manage.ServerRegistry;
import org.liujiaxin.jrpc.transfer.RpcDecoder;
import org.liujiaxin.jrpc.transfer.RpcEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServer implements ApplicationContextAware, InitializingBean {

    private ServerRegistry serverRegistry;

    private ServerInfo serverInfo;

    private HashMap<InterfaceInfo, Object> services = new HashMap<InterfaceInfo, Object>();

    public HashMap<InterfaceInfo, Object> getServices() {
        return services;
    }

    public void setServices(HashMap<InterfaceInfo, Object> services) {
        this.services = services;
    }

    public RpcServer(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerRegistry getServerRegistry() {
        return serverRegistry;
    }

    public void setServerRegistry(ServerRegistry serverRegistry) {
        this.serverRegistry = serverRegistry;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);

        if (serviceBeanMap != null && !serviceBeanMap.isEmpty()) {
            for (Object bean : serviceBeanMap.values()) {
                String interfaceName = bean.getClass().getAnnotation(RpcService.class).value().getName();
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setInterfaceName(interfaceName);
                services.put(interfaceInfo, bean);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new RpcDecoder(RpcRequest.class)).addLast(
                            new RpcEncoder(RpcResponse.class)).addLast(new RpcHandler(services));
                    }
                }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            String serverAddress = serverInfo.getServerAddr();
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();

            if (serverRegistry != null) {
                serverRegistry.register(serverInfo);
            }
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
