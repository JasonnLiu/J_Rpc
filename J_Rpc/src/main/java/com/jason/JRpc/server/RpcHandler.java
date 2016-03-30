package com.jason.JRpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;

import com.jason.JRpc.entity.RpcRequest;

public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest>{

	public RpcHandler(HashMap<InterfaceInfo, Object> services) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcRequest arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
