package com.imooc.wsdemo.spring;

import com.imooc.wsdemo.java.WebSocketConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

@Configuration  //声明配置类
@EnableWebSocket  //启用了WebSocket
public class MyWsConfig implements WebSocketConfigurer {
    @Resource
    MyWsHandler myWsHandler;   //注入myWsHandler
    @Resource
    MyWsInterceptor myWsInterceptor; //注入握手拦截器

    /**注册WebSocketHandler方法
     * registerWebSocketHandlers是Spring WebSocket中的一个方法，它位于WebSocketConfigurer接口中，用于注册WebSocket处理器。
     * WebSocketConfigurer接口包含一个registerWebSocketHandlers方法，您可以通过实现该接口并重写该方法来注册WebSocket处理器。
     * WebSocket处理器是指处理WebSocket连接建立、消息接收、消息发送等逻辑的具体类。
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /**注册 WebSocket 处理器
         * registry.addHandler是Spring WebSocket中的一个方法，它位于WebSocketHandlerRegistry类中，用于注册 WebSocket 处理器。
         * WebSocketHandlerRegistry类包含一个addHandler方法，您可以通过该方法来注册WebSocket处理器。WebSocket处理器是指处理
         * WebSocket连接建立、消息接收、消息发送等逻辑的具体类。
         * addHandler: 参数二为监听的地址
         * addInterceptors: 握手拦截器
         * setAllowedOrigins: 允许的源
         */
        registry.addHandler(myWsHandler, "/myWs1").addInterceptors(myWsInterceptor).setAllowedOrigins("*");
    }
}
