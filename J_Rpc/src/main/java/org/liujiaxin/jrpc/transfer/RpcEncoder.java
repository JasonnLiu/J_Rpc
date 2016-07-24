package org.liujiaxin.jrpc.transfer;

import org.liujiaxin.jrpc.util.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class RpcEncoder extends MessageToByteEncoder<Object> {

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
