package com.starimmortal.websocket.configuration;

import com.starimmortal.websocket.message.DefaultWebSocketHandler;
import com.starimmortal.websocket.message.WebSocket;
import com.starimmortal.websocket.message.WebSocketImpl;
import com.starimmortal.websocket.message.WebSocketInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置项
 *
 * @author william@StarImmortal
 * @date 2022/09/20
 */
@Configuration
@ConditionalOnProperty(prefix = "websocket", value = "enable", havingValue = "true")
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Value("${websocket.intercept:false}")
    private boolean intercepted;

    @Bean
    public DefaultWebSocketHandler defaultWebSocketHandler() {
        return new DefaultWebSocketHandler();
    }

    @Bean
    public WebSocket webSocket() {
        return new WebSocketImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "websocket", value = "intercept", havingValue = "true")
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        if (intercepted) {
            registry.addHandler(defaultWebSocketHandler(), "ws/message")
                    .addInterceptors(webSocketInterceptor())
                    .setAllowedOrigins("*");
            return;
        }
        registry.addHandler(defaultWebSocketHandler(), "ws/message")
                .setAllowedOrigins("*");
    }
}
