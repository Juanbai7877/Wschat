package org.websocketchat.websocketchat.config;

import jakarta.websocket.server.ServerEndpoint;
import org.apache.catalina.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author ALL
 * @date 2024/4/19
 * @Description
 */
@Configuration
public class WebsocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
