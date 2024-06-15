package com.synergy.binarfood.chat.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketIOConfig {
    private SocketIOServer socketIOServer;

    @Value("${socket-io.hostname}")
    private String hostname;

    @Value("${socket-io.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname(this.hostname);
        configuration.setPort(this.port);

        this.socketIOServer = new SocketIOServer(configuration);
        this.socketIOServer.start();

        this.socketIOServer.addConnectListener(socketIOClient -> {
            log.info("[ CONNECTED CLIENT ] - A new client is connected to {}", socketIOClient.getSessionId());
        });

        this.socketIOServer.addDisconnectListener(socketIOClient -> {
            log.info("[ DISCONNECTED CLIENT ] - A new client is connected to {}", socketIOClient.getSessionId());
        });

        return this.socketIOServer;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.socketIOServer.stop();
    }
}
