package tw.niq.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootMicroUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMicroUserApplication.class, args);
	}

}
