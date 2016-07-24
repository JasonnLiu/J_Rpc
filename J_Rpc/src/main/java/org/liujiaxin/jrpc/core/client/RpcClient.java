package org.liujiaxin.jrpc.core.client;

import org.liujiaxin.jrpc.entity.RpcRequest;
import org.liujiaxin.jrpc.entity.RpcResponse;
import org.liujiaxin.jrpc.transfer.RpcDecoder;
import org.liujiaxin.jrpc.transfer.RpcEncoder;

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
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {

                    channel.pipeline().addLast(new RpcEncoder(RpcRequest.class))
                                      .addLast(new RpcDecoder(RpcResponse.class))
                                      .addLast(RpcClient.this);
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, Integer.parseInt(port)).sync();
            future.channel().writeAndFlush(req).sync();
            synchronized (obj) {
                obj.wait();
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
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response;
        synchronized (obj) {
            obj.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
