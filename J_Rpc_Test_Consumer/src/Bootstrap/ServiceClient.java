package Bootstrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jason.JRpc.client.RpcProxy;

import service.HelloService;

public class ServiceClient {
	
	private HelloService helloService;

	public HelloService gethelloService() {
		return helloService;
	}

	public void sethelloService(HelloService helloService) {
		this.helloService = helloService;
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cli-spring.xml");
		//ServiceClient serviceClient = (ServiceClient) context.getBean("serviceClient");
		
		RpcProxy pro = (RpcProxy) context.getBean("helloServiceRpcProxy");
		
		
		HelloService hs = (HelloService)pro.newProxy(HelloService.class);
		
		String result = hs.hello("word");
		System.out.println(result);

	}

}
