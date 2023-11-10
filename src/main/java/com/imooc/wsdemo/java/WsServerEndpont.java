package com.imooc.wsdemo.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听WebSocket 地址/myWs
 */
@ServerEndpoint("/myWs")   //类监听的链接地址
@Component   //被Spring Boot扫描到，注入项目中
@Slf4j
public class WsServerEndpont {
    //static保证变量是属于类的，而不是属于对象的；ConcurrentHashMap是线程安全的
    static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    // 连接建立时执行的操作
    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(),session);
        log.info("websocket is open");
    }
    //收到客户端消息时执行的操作
    @OnMessage
    public String onMessage(String text){
        log.info("收到一条消息：" + text);
        return "已收到你的消息";
    }
    // 连接关闭的时候执行的操作
    @OnClose
    public void onClose(Session session){
        sessionMap.remove(session.getId());
        log.info("websocket is close");
    }
    //每间隔2秒发送给客户端心跳消息
    @Scheduled(fixedRate = 2000)
    public void sendmsg() throws IOException {
        for (String key:sessionMap.keySet()){
            sessionMap.get(key).getBasicRemote().sendText("靓仔");
        }
    }
}
