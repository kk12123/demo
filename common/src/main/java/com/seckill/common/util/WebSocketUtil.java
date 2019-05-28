package com.seckill.common.util;

import com.alibaba.fastjson.JSON;
import com.seckill.common.dto.PurchaseProductInfo;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kk
 * @since 2019.5.26
 */
@ServerEndpoint("/webSocket/{userNo}")
public class WebSocketUtil {
    private static int onlineCount = 0;
    private static Map<String, WebSocketUtil> clients = new ConcurrentHashMap<>();
    private Session session;
    private String userNo;

    @OnOpen
    public void onOpen(@PathParam("userNo") String userNo, Session session){

        this.userNo = userNo;
        this.session = session;

        addOnlineCount();
        clients.put(userNo, this);
        System.out.println("已连接");
    }

    @OnClose
    public void onClose() {
        clients.remove(userNo);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message){

        PurchaseProductInfo purchaseProductInfo = JSON.parseObject(message,PurchaseProductInfo.class);
        sendMessageTo(purchaseProductInfo);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    protected void sendMessageTo(PurchaseProductInfo purchaseProductInfo) {
        String message;
        for (WebSocketUtil item : clients.values()) {
            if (item.userNo.equals(purchaseProductInfo.getUserNo())){
                message = JSON.toJSONString(purchaseProductInfo);
                item.session.getAsyncRemote().sendText(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketUtil.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketUtil.onlineCount--;
    }
}
