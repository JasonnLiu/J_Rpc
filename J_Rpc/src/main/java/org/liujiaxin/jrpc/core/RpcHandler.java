package org.liujiaxin.jrpc.core;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.liujiaxin.jrpc.entity.RpcRequest;
import org.liujiaxin.jrpc.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RpcHandler.class);
	
	private HashMap<InterfaceInfo, Object> services;

	public RpcHandler(HashMap<InterfaceInfo, Object> services) {
		this.services = services;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest req)
			throws Exception {
		RpcResponse response = new RpcResponse();
		response.setRequestId(req.getRequestId());
		try {
			Object result = handle(req);
			LOGGER.info("handle method");
			response.setResult(result);
		} catch (Throwable t) {
			t.printStackTrace();
			response.setError(t);
		}
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

	private Object handle(RpcRequest req) throws InvocationTargetException {
		String className = req.getClassName();
		InterfaceInfo i = new InterfaceInfo();
		i.setInterfaceName(className);
		LOGGER.info(className);
		
        Object serviceBean = services.get(i);
        if(serviceBean == null){
        	 LOGGER.error("get no serviceBean");
        }
       

        Class<?> serviceClass = serviceBean.getClass();
        
        String methodName = req.getMethodName();
        Class<?>[] parameterTypes = req.getParameterTypes();
        Object[] parameters = req.getParameters();

        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
	}

}
