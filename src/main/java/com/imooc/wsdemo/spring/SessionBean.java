package com.imooc.wsdemo.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor   //生成所有参数构造方法
@Data   //生成所有getter、setter方法
public class SessionBean {
    private WebSocketSession webSocketSession;
    private Integer clientId;
}
