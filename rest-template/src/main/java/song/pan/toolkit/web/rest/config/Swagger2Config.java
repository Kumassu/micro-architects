package song.pan.toolkit.web.rest.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.util.*;

@Configuration
@Controller
public class Swagger2Config {


    @GetMapping
    public String doc() {
        return "redirect:/swagger-ui.html";
    }


    @Bean
    public Docket openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Rest API")
                        .description("")
                        .version("1.0.0")
                        .termsOfServiceUrl(null)
                        .license(null)
                        .licenseUrl(null)
                        .build()
                )
                .groupName("OpenApi")
                .tags(new Tag("Book", "", 1))
                .select()
                .apis(RequestHandlerSelectors.basePackage("song.pan.toolkit.web.rest"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                // could add multiple security schemes
                .securitySchemes(List.of(bearer(), basic()))
                // use security contexts to mapping url & method & securityScheme
                .securityContexts(securityContexts());
    }


    SecurityScheme bearer() {
        ImplicitGrant implicitGrant = new ImplicitGrant(new LoginEndpoint("${authorize_url}"), "${access_token}");
        return new OAuth("Bearer", List.of(new AuthorizationScope("read", ""), new AuthorizationScope("write", "")),
                List.of(implicitGrant));
    }


    SecurityScheme basic() {
        return new BasicAuth("Basic");
    }


    List<SecurityContext> securityContexts() {
        LinkedList<SecurityContext> securityContexts = new LinkedList<>();
        List<SecurityReference> bearer = List.of(new SecurityReference("Bearer", new AuthorizationScope[]{}));
        List<SecurityReference> basic = List.of(new SecurityReference("Basic", new AuthorizationScope[]{}));
        securityContexts.add(securityContext("/api/v*/books", basic, HttpMethod.GET));
        securityContexts.add(securityContext("/api/v*/books/*", basic, HttpMethod.GET));
        securityContexts.add(securityContext("/api/v*/caches", basic, HttpMethod.GET));
        securityContexts.add(securityContext("/api/v*/books", bearer, HttpMethod.POST));
        securityContexts.add(securityContext("/api/v*/books", bearer, HttpMethod.PUT));
        securityContexts.add(securityContext("/api/v*/books", bearer, HttpMethod.PATCH));
        securityContexts.add(securityContext("/api/v*/books/*", bearer, HttpMethod.DELETE));
        return securityContexts;
    }


    SecurityContext securityContext(String url, List<SecurityReference> securityReferences, HttpMethod...httpMethods) {
        return SecurityContext.builder()
                .securityReferences(securityReferences)
                .forPaths(PathSelectors.ant(url))
                .forHttpMethods(Objects.isNull(httpMethods) || httpMethods.length == 0 ? Predicates.alwaysTrue() :
                        Arrays.stream(httpMethods).map(Predicates::equalTo).reduce(Predicates.alwaysFalse(), Predicates::or))
                .build();
    }


    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
                .clientId("${client_id}")
                .clientSecret("${client_secret}")
                .scopeSeparator(" ")
                .build();
    }


    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
                .deepLinking(false)
                .displayOperationId(false)
                .defaultModelsExpandDepth(0)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(true)
                .docExpansion(DocExpansion.LIST)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }

}
