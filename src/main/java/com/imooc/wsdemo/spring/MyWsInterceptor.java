package com.imooc.wsdemo.spring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截器HttpSessionHandshakeInterceptor 是Spring WebSocket中的一个拦截器，它位于WebSocketConfigurer接口中，
 * 用于在WebSocket连接建立之前对请求进行拦截和处理。
 * HttpSessionHandshakeInterceptor 可以用来实现一些诸如用户身份验证、日志记录、权限检查等功能。它能够拦截WebSocket
 * 连接建立之前的请求，并在请求处理结束后将 HttpSession 对象传递给 WebSocket 连接的 session 属性。
 */
@Component
@Slf4j
public class MyWsInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * beforeHandshake是Spring WebSocket中的一个方法,它位于WebSocketConfigurer接口中，用于配置WebSocket连接之前的处理逻辑。
     * 在 WebSocket连接之前，Spring会调用WebSocketConfigurer接口中的beforeHandshake方法，您可以在该方法中添加一些逻辑来处理
     * WebSocket连接之前的请求。例如，您可以检查请求头中的认证信息，或者向请求头中添加一些自定义的属性。
     * @param request the current request
     * @param response the current response
     * @param wsHandler the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     * session; the provided attributes are copied, the original map is not used.
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info(request.getRemoteAddress().toString()+"开始握手");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /**
     * afterHandshake 是Spring WebSocket中的一个方法，它位于WebSocketConfigurer接口中，用于配置WebSocket连接建立之后的处理逻辑。
     * 在 WebSocket连接建立之后，Spring会调用 WebSocketConfigurer接口中的afterHandshake方法，您可以在该方法中添加一些逻辑来处理
     * WebSocket连接建立之后的请求。例如，您可以向请求头中添加一些自定义的属性，或者向WebSocket连接的session属性中添加一些数据。
     * @param request the current request
     * @param response the current response
     * @param wsHandler the target WebSocket handler
     * @param ex an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info(request.getRemoteAddress().toString()+"完成握手");
        //完成握手后将http协议升级为WebSocket协议
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
