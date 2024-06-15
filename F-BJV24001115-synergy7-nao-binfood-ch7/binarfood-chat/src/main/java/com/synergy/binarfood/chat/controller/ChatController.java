package com.synergy.binarfood.chat.controller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.synergy.binarfood.chat.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatController {
    private final SocketIOServer socketIOServer;

    public ChatController(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        this.socketIOServer.addConnectListener(onUserConnected);
        this.socketIOServer.addDisconnectListener(onUserDisconnected);
        this.socketIOServer.addEventListener("room", Message.class, onMessageSent);
    }

    private ConnectListener onUserConnected = socketIOClient -> {
        log.info("[ SOCKET CONNECTED ] ");
    };

    private DisconnectListener onUserDisconnected = socketIOClient -> {
        log.info("[ SOCKET DISCONNECTED ] ");
    };

    private DataListener<Message> onMessageSent = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient socketIOClient, Message message, AckRequest ackRequest) throws Exception {
            socketIOServer.getBroadcastOperations()
                    .sendEvent(message.getTo(), message.getContent());
            ackRequest.sendAckData("Message sent to " + message.getTo());
        }
    };
}
