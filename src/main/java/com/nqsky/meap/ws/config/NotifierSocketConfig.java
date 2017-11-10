package com.nqsky.meap.ws.config;

import com.nqsky.meap.ws.handler.QuestionHandler;
import com.nqsky.meap.ws.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Controller
public class NotifierSocketConfig implements WebSocketConfigurer{

    @Autowired
    private NotificationService broadcasterService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new QuestionHandler(), "/question").setAllowedOrigins("*");
        //.withSockJS();

        registry.addHandler(broadcasterService, "/notifier").setAllowedOrigins("*");
    }

}
