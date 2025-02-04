package com.rade.protect.api;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiPortConfig {

    @Value("${server.api.port}")
    private int apiPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> apiServerCustomizer() {
        return factory -> {
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setPort(apiPort);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }

}
