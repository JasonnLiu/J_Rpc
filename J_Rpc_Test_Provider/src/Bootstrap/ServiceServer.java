package Bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.HelloService;

import com.jason.JRpc.annotation.RpcService;
import com.jason.JRpc.server.InterfaceInfo;
import com.jason.JRpc.server.JRpcServer;
import com.jason.JRpc.server.RpcServer;

public class ServiceServer {

	private String address;

	private HelloService helloService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServiceServer.class);

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
		LOGGER.debug("start");
		LOGGER.info("a");
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"ser-spring.xml");
		/*
		 * InterfaceInfo i = new InterfaceInfo(); i.setInterfaceName("a");
		 * Map<InterfaceInfo, Integer> m = new HashMap<InterfaceInfo,
		 * Integer>(); m.put(i, 1); InterfaceInfo i1 = new InterfaceInfo();
		 * i1.setInterfaceName("a"); System.out.println(i.equals(i1)); int n =
		 * 0; n = m.get(i1); System.out.println(n);
		 */

		// RpcServer serviceServer = (RpcServer)
		// context.getBean("helloServiceImpl");
		// serviceServer.start();

		// Map<String, Object> map = serviceServer.getServices();
		// HelloService helloService = (HelloService) map.get("helloService");
		// HelloService helloService = (HelloService)
		// context.getBean("helloServiceImpl");
		// String result = helloService.hello("word");
		// System.out.println(result);

	}

}
