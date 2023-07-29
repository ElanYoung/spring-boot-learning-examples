package com.starimmortal.websocket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置项
 *
 * @author william@StarImmortal
 * @date 2022/09/20
 */
@Configuration
public class WebSocketConfiguration {

	/**
	 * 注入ServerEndpointExporter， 这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}
