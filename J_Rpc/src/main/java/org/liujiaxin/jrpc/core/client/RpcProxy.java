package org.liujiaxin.jrpc.core.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.liujiaxin.jrpc.core.InterfaceInfo;
import org.liujiaxin.jrpc.entity.RpcRequest;
import org.liujiaxin.jrpc.entity.RpcResponse;
import org.liujiaxin.jrpc.manage.ServerDiscovery;

public class RpcProxy {

    private InterfaceInfo interfaceInfo;

    private ServerDiscovery serverDiscovery;

    public RpcProxy(InterfaceInfo interfaceInfo, ServerDiscovery serverDiscovery) {
        this.interfaceInfo = interfaceInfo;
        this.serverDiscovery = serverDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<?> interfaceClazz) {
        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class<?>[]{ interfaceClazz },
            new InvocationHandler() {

                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setClassName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);

                    RpcClient client = null;
                    if (serverDiscovery != null) {
                        String addr = serverDiscovery.discover(interfaceInfo);
                        String[] addrArr = addr.split(":");
                        String host = addrArr[0];
                        String port = addrArr[1];
                        client = new RpcClient(host, port);
                    }

                    RpcResponse resp = null;
                    if (client != null) {
                        resp = client.send(request);
                    }

                    if (resp.isError()) {
                        resp.getError().printStackTrace();
                        throw resp.getError();
                    } else {
                        return resp.getResult();
                    }
                }
            });
    }
}
