package ma.cam;

import java.time.LocalDate;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@Slf4j
public class AuthServerApplication {

	public static void main(String[] args) {
		log.debug("Starting AuthenticationServer");
		SpringApplication.run(AuthServerApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		log.info("AuthenticationServer started, server time {} , timezone {}", LocalDate.now(), TimeZone.getDefault());
	}
}
