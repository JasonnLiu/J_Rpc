package com.jason.JRpc.util;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import com.jason.JRpc.entity.RpcRequest;
import com.jason.JRpc.entity.RpcResponse;

public class RpcDecoder extends ByteToMessageDecoder {

	public RpcDecoder(Class<?> clazz) {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1,
			List<Object> arg2) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
