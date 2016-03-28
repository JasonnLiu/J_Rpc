package Bootstrap;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.HelloService;

import com.jason.JRpc.server.JRpcServer;
import com.jason.JRpc.server.RpcServer;

public class ServiceServer {

	private String address;

	private HelloService helloService;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public HelloService gethelloService() {
		return helloService;
	}

	public void sethelloService(HelloService helloService) {
		this.helloService = helloService;
	}

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"ser-spring.xml");
		RpcServer serviceServer = (RpcServer) context.getBean("rpcServer");
		serviceServer.start();

		/*
		 * Map<String, Object> map = serviceServer.getServices(); HelloService
		 * helloService = (HelloService) map.get("helloService"); String result
		 * = helloService.hello("word"); System.out.println(result);
		 */
	}

}
