package service;

import com.jason.JRpc.annotation.RpcService;



@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

	public String hello(String name) {
		return "hello " + name + "!";
	}

}
