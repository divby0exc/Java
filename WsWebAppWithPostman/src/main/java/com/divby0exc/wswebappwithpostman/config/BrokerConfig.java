package com.divby0exc.wswebappwithpostman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class BrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry path) {
        path.enableSimpleBroker("/sub");

        for(int i = 0; i < userDefinedEndpoint.getEndpoints().size(); i++) {
            userDefinedEndpoint.getEndpoints().get(i);
        }
        path.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");
        registry.addEndpoint(userDefinedEndpoint.getEndpoints());
        registry.addEndpoint("/ws").withSockJS();
    }
}
