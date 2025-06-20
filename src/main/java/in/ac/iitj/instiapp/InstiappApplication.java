package in.ac.iitj.instiapp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.sql.DataSource;

@SpringBootApplication
public class InstiappApplication {
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void logDbInfo() throws Exception {
		System.out.println("[DEBUG] Connected to DB: " + dataSource.getConnection().getMetaData().getURL());
	}

	public static void main(String[] args) {
		SpringApplication.run(InstiappApplication.class, args);
	}

}
