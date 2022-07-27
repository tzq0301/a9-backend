package a9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class AuthApplication {
    @GetMapping("/")
    public String index() {
        return "Index";
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
