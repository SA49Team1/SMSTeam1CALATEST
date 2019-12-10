package sg.edu.nus.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"sg.edu.nus.sms"})
public class SmsTeam1Application {

	public static void main(String[] args) {
		SpringApplication.run(SmsTeam1Application.class, args);
	}

}
