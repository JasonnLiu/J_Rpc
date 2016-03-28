package Bootstrap;

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
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring.xml");
		ServiceClient serviceClient = (ServiceClient) context.getBean("serviceClient");
		String result = serviceClient.gethelloService().hello("word");
		System.out.println(result);

	}

}
