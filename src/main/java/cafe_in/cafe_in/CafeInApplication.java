package cafe_in.cafe_in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class CafeInApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeInApplication.class, args);
	}

}
