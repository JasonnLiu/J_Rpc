package Bootstrap;

import org.liujiaxin.jrpc.core.client.RpcProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        ApplicationContext context = new ClassPathXmlApplicationContext("cli-spring.xml");

        RpcProxy proxy = (RpcProxy) context.getBean("helloServiceRpcProxy");

        HelloService hs = (HelloService) proxy.newProxy(HelloService.class);
		try {
			String result = hs.hello("word");
			System.out.println(result);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
