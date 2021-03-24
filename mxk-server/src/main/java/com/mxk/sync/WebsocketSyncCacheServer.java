package com.mxk.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mxk.cache.RouteRuleCache;
import com.mxk.constants.OperationTypeEnum;
import com.mxk.pojo.dto.AppRuleDTO;
import com.mxk.pojo.dto.RouteRuleOperationDTO;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Websocket 监听
 */
public class WebsocketSyncCacheServer extends org.java_websocket.server.WebSocketServer {

    private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WebsocketSyncCacheServer.class);

    private Gson gson = new GsonBuilder().create();

    private MessageHandler messageHandler;

    public WebsocketSyncCacheServer(Integer port) {
        super(new InetSocketAddress(port));
        this.messageHandler = new MessageHandler();
    }


    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        LOGGER.info("server is open");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        LOGGER.info("websocket server close...");
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        LOGGER.info("websocket server receive message:\n[{}]", message);
        this.messageHandler.handler(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        LOGGER.info("websocket server start...");
    }


    class MessageHandler {

        public void handler(String message) {
            RouteRuleOperationDTO operationDTO = gson.fromJson(message, RouteRuleOperationDTO.class);
            if (CollectionUtils.isEmpty(operationDTO.getRuleList())) {
                return;
            }
            Map<String, List<AppRuleDTO>> map = operationDTO.getRuleList()
                    .stream().collect(Collectors.groupingBy(AppRuleDTO::getAppName));
            if (OperationTypeEnum.INSERT.getCode().equals(operationDTO.getOperationType())
                    || OperationTypeEnum.UPDATE.getCode().equals(operationDTO.getOperationType())) {
                RouteRuleCache.add(map);
            } else if (OperationTypeEnum.DELETE.getCode().equals(operationDTO.getOperationType())) {
                RouteRuleCache.remove(map);
            }
        }
    }
}
