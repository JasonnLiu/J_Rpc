package org.liujiaxin.jrpc.transfer;

import java.util.List;

import org.liujiaxin.jrpc.util.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


public class RpcDecoder extends ByteToMessageDecoder {
	
	private Class<?> clazz;

	public RpcDecoder(Class<?> clazz) {
		this.clazz = clazz;
	}

	

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = SerializationUtil.deserialize(data, clazz);
        out.add(obj);
		
	}

}
