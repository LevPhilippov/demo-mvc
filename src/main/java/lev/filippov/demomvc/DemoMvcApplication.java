package lev.filippov.demomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScans(value = @ComponentScan("lev.filippov"))
public class DemoMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoMvcApplication.class, args);
    }

}
