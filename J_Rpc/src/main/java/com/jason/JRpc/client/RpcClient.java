package com.jason.JRpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.jason.JRpc.entity.RpcRequest;
import com.jason.JRpc.entity.RpcResponse;
import com.jason.JRpc.util.RpcDecoder;
import com.jason.JRpc.util.RpcEncoder;

public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

	private String host;
	private String port;

	private RpcResponse response;

	private final Object obj = new Object();

	public RpcClient(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public RpcResponse send(RpcRequest req) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel)
								throws Exception {
							channel.pipeline()
									.addLast(new RpcEncoder(RpcRequest.class)) //将RPC请求进行编码（为了发送请求）
									.addLast(new RpcDecoder(RpcResponse.class)) //将RPC响应进行解码（为了处理响应）
									.addLast(RpcClient.this); //使用 RpcClient发送RPC请求
						}
					}).option(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.connect(host, Integer.parseInt(port)).sync();
			future.channel().writeAndFlush(req).sync();

			synchronized (obj) {
				obj.wait(); // 未收到响应，使线程等待
			}

			if (response != null) {
				future.channel().closeFuture().sync();
			}
			return response;
		} finally {
			group.shutdownGracefully();
		}

	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcResponse arg1)
			throws Exception {
		this.response = response;
		synchronized (obj) {
			obj.notifyAll(); // 收到响应，唤醒线程
		}
	}
}
