package com.jason.JRpc.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.jason.JRpc.entity.RpcResponse;

public class RpcEncoder extends MessageToByteEncoder {

	public RpcEncoder(Class<?> clazz) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
