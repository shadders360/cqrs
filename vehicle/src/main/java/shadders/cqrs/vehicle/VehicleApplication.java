package shadders.cqrs.vehicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
@SpringBootApplication
public class VehicleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(VehicleApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.debug("Spring Boot Beans start:");
                String[] beanNames = ctx.getBeanDefinitionNames();
                Arrays.sort(beanNames);
                for (String beanName : beanNames) {
                    LOGGER.debug(beanName);
                }
                LOGGER.debug("Spring Boot Beans end:");
            }

        };
    }



}
