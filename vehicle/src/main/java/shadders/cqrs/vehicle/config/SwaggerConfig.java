package shadders.cqrs.vehicle.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * shadders on 25/05/2017.
 *
 * @since 1.0.0
 */
@Configuration
@EnableSwagger2 //Enable swagger 2.0 spec
public class SwaggerConfig {

    @Bean
    public Docket vehicleApi() {
        return new Docket(DocumentationType.SWAGGER_2)
              //  .groupName("vehicle-api") if another endpoint is created
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("shadders.cqrs.vehicle.resource"))
                .paths(vehiclePaths())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Demo vehicle Api")
                .description("This API is a POC for a vehicle domain using CQRS/Event Source design patterns.\n"
                        + "The REST service is built using springboot"
                        + " and the CRQS/Eventsource implementation using the axon framework."
                        + "The query model is exposed using spring-data-rest project accessed here http://localhost:8080")
                .version("1.0")
                .build();
    }

    /**
     * process only matching endpoints
     * @return predicate of combined using OR
     */
    private Predicate<String> vehiclePaths() {
        return or(
                regex("/api/vehicles.*")
              //  regex("/register.*")
        );

    }


}
