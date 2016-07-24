package service;

import org.liujiaxin.jrpc.annotation.RpcService;



@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

	public String hello(String name) {
		return "hello " + name + "!";
	}

}
