package br.com.erudio.config;

import br.com.erudio.serialization.converter.YamlJackson2HttpMesageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        https://www.baeldung.com/spring-mvc-content-negotiation-json-xml

        // VIA QUERY PARAM. http://localhost:8080/api/person/v1?mediaType=xml
        /*configurer.favorParameter(true)
                .parameterName("mediaType").ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);*/

        // via HEADER PARAM. http://localhost:8080/api/person/v1
        //header (Accept) - (application/xml)
        configurer.favorParameter(false)
              .ignoreAcceptHeader(false)
              .useRegisteredExtensionsOnly(false)
              .defaultContentType(MediaType.APPLICATION_JSON)
                    .mediaType("json", MediaType.APPLICATION_JSON)
                    .mediaType("xml", MediaType.APPLICATION_XML)
                    .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
    }

    // via HEADER PARAM. http://localhost:8080/api/person/v1
    //header (Accept) - (application/x-yaml)
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
       converters.add(new YamlJackson2HttpMesageConverter());

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       var allowedOrigins = corsOriginPatterns.split(",");
       registry.addMapping("/**")
               //.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") setando por verbos
               .allowedMethods("*") // todos
               .allowedOrigins(allowedOrigins)
       .allowCredentials(true);
    }
}
