package com.jason.JRpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import com.jason.JRpc.entity.RpcRequest;
import com.jason.JRpc.entity.RpcResponse;
import com.jason.JRpc.server.InterfaceInfo;

public class RpcProxy {
	private InterfaceInfo interfaceInfo;
	private ServerDiscovery serverDiscovery;

	public RpcProxy(InterfaceInfo interfaceInfo, ServerDiscovery serverDiscovery) {
		this.interfaceInfo = interfaceInfo;
		this.serverDiscovery = serverDiscovery;
	}

	@SuppressWarnings("unchecked")
	public <T> T newProxy(Class<?> interfaceClazz) {
		return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
				new Class<?>[] { interfaceClazz }, new InvocationHandler() {

					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {

						RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
						request.setRequestId(UUID.randomUUID().toString());
						request.setClassName(method.getDeclaringClass()
								.getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);

						RpcClient client = null;
						if (serverDiscovery != null) {
							String addr = serverDiscovery.discover(interfaceInfo);
							String[] addrArr = addr.split(":");
							String host = addrArr[0];
							String port = addrArr[1];
							client = new RpcClient(host,port);
							
						}
						RpcResponse rep = null;
						if(client != null){
							rep = client.send(request);
						}
						if (rep.isError()) {
	                        throw rep.getError();
	                    } else {
	                        return rep.getResult();
	                    }
					}
				});
	}
}
