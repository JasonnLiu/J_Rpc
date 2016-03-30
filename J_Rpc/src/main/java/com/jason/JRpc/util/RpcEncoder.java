package com.jason.JRpc.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.jason.JRpc.entity.RpcResponse;

public class RpcEncoder extends MessageToByteEncoder {

	private Class<?> clazz;

	public RpcEncoder(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out)
			throws Exception {
		if (clazz.isInstance(in)) {
			byte[] data = SerializationUtil.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}

	}

}
