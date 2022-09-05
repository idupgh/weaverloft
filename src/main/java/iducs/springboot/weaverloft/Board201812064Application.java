package iducs.springboot.weaverloft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing
@SpringBootApplication
public class Board201812064Application {

    public static void main(String[] args) {
        SpringApplication.run(Board201812064Application.class, args);
    }
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() { //put, delete 처리
        return new HiddenHttpMethodFilter();
    }
}
