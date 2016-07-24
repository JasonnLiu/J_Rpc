package Bootstrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.HelloService;

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

        ApplicationContext context = new ClassPathXmlApplicationContext("ser-spring.xml");

	}

}
