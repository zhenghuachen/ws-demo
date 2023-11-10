package com.imooc.wsdemo.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket主处理程序
 * AbstractWebSocketHandler是Spring WebSocket中的一个抽象类，它提供了一些基本的WebSocket处理方法，
 * 例如 handleMessage、handleError、finish、sendMessage 等。
 * 这些方法可以被继承并重写，以便实现自定义的WebSocket处理逻辑。例如，您可以重写handleMessage方法来处理
 * 收到的消息，或者重写sendMessage方法来发送消息给客户端。
 */
@Component
@Slf4j
public class MyWsHandler extends AbstractWebSocketHandler {
    private static Map<String, SessionBean> sessionBeanMap;
    private static AtomicInteger clientidMaker;  //线程安全Interger
    private static StringBuffer stringBuffer;  //StringBuffer线程安全且可变
    static {
        sessionBeanMap = new ConcurrentHashMap<>();
        clientidMaker = new AtomicInteger(0);
        stringBuffer = new StringBuffer();
    }

    /** 连接建立
     * afterConnectionEstablished 是Spring WebSocket中的一个方法，它位于AbstractWebSocketHandler类中，
     * 用于配置WebSocket连接建立之后的处理逻辑。
     * 在WebSocket连接建立之后，Spring会调用AbstractWebSocketHandler类中的afterConnectionEstablished方法，
     * 您可以在该方法中添加一些逻辑来处理WebSocket连接建立之后的请求。例如，您可以向请求头中添加一些自定义的属性，
     * 或者向 WebSocket 连接的 session 属性中添加一些数据。
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session, clientidMaker.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
        log.info(sessionBeanMap.get(session.getId()).getClientId()+":"+"建立链接");
        stringBuffer.append(sessionBeanMap.get(session.getId()).getClientId()+":"+"进入了群聊<br/>");
        sendMessage(sessionBeanMap);
    }

    /** 收到消息
     * handleTextMessage 是 Spring WebSocket 中的一个方法，它位于AbstractTextWebSocketHandler类中，
     * 用于处理文本类型的WebSocket消息。
     * handleTextMessage方法接收一个WebSocketMessage<?>类型的参数，该参数表示收到的文本消息。您可以在该方法中
     * 添加一些逻辑来处理收到的文本消息，例如将其转换为其他格式、进行过滤等。
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info(sessionBeanMap.get(session.getId()).getClientId()+":"+ message.getPayload());
        stringBuffer.append(sessionBeanMap.get(session.getId()).getClientId()+":"+ message.getPayload()+"<br/>");
        sendMessage(sessionBeanMap);
    }

    /**传输异常
     * handleTransportError 是 Spring WebSocket 中的一个方法，它位于 AbstractWebSocketHandler 类中，
     * 用于处理 WebSocket 传输错误。
     * 当WebSocket连接发生错误时，Spring会调用AbstractWebSocketHandler类中的handleTransportError方法，您可以在该方法中
     * 添加一些逻辑来处理连接错误。例如，您可以向请求头中添加一些自定义的属性，或者向WebSocket连接的session属性中添加一些数据。
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if(session.isOpen()){ //出错后如果链接打开的，关闭
            session.close();
        }
        sessionBeanMap.remove(session.getId());
    }

    /** 连接关闭
     * afterConnectionClosed是Spring WebSocket中的一个方法，它位于AbstractWebSocketHandler类中，用于处理WebSocket连接关闭后的逻辑。
     * 当WebSocket连接关闭时，Spring会调用AbstractWebSocketHandler类中的afterConnectionClosed方法，您可以在该方法中添加一些逻辑来处理
     * 连接关闭后的请求。例如，您可以向请求头中添加一些自定义的属性，或者向WebSocket连接的session属性中添加一些数据。
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        int clientId = sessionBeanMap.get(session.getId()).getClientId();
        sessionBeanMap.remove(session);
        log.info(clientId +"关闭链接");
        stringBuffer.append(clientId+":"+ "退出了群聊<br/>");
        sendMessage(sessionBeanMap);
    }
    //每间隔2秒发送给客户端心跳消息
//    @Scheduled(fixedRate = 2000)
//    public void sendmsg() throws IOException {
//        for (String key:sessionBeanMap.keySet()){
//            sessionBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage("靓仔"));
//        }
//    }
    public  void sendMessage(Map<String, SessionBean> sessionBeanMap){
        for(String key: sessionBeanMap.keySet()){
            try {
                sessionBeanMap.get(key).getWebSocketSession().sendMessage(new TextMessage(stringBuffer.toString()));
            } catch (IOException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }
    }
}
