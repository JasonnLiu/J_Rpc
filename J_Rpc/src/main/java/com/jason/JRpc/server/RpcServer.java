package com.jason.JRpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jason.JRpc.annotation.RpcService;
import com.jason.JRpc.entity.RpcRequest;
import com.jason.JRpc.entity.RpcResponse;
import com.jason.JRpc.util.RpcDecoder;
import com.jason.JRpc.util.RpcEncoder;

public class RpcServer implements JRpcServer, ApplicationContextAware,
		InitializingBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

	private ServerRegistry serverRegistry;
	private ServerInfo serverInfo;

	// key为代表某一种版本的接口，value为该特定接口的实现类
	private HashMap<InterfaceInfo, Object> services;

	public HashMap<InterfaceInfo, Object> getServices() {
		return services;
	}

	public void setServices(HashMap<InterfaceInfo, Object> services) {
		this.services = services;
	}

	public RpcServer(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public void start() {

	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		Map<String, Object> serviceBeanMap = ctx
				.getBeansWithAnnotation(RpcService.class);
		if (serviceBeanMap != null && serviceBeanMap.isEmpty()) {
			for (Object bean : serviceBeanMap.values()) {
				String interfaceName = bean.getClass()
						.getAnnotation(RpcService.class).value().getName();
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
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                            .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                            .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                            .addLast(new RpcHandler(services)); // 处理 RPC 请求
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            String serverAddress = serverInfo.getServerAddr();
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            LOGGER.debug("server started on port {}", port);

            if (serverRegistry != null) {
                serverRegistry.register(serverInfo); // 注册服务地址
            }

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}

}
