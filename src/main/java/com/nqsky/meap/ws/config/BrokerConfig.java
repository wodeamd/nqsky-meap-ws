package com.nqsky.meap.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Random;

//@Configuration
//@EnableWebSocketMessageBroker
//@Controller
public class BrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/question1").setHandshakeHandler(new RandomUsernameHandshakeHandler()).setAllowedOrigins("*").withSockJS();
        
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/topic", "/queue") ;
    }



}

class RandomUsernameHandshakeHandler extends DefaultHandshakeHandler{
    private String[] AJECTIVES = {"aggressive","annoyed","black","beutiful","crazy"};
    private String[] NOUNS = {"agent","americian","caiman","crab"};
    
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String username = getRandom(AJECTIVES) + "-" + getRandom(NOUNS) + "-" + random(Integer.MAX_VALUE);
        return new Principal() {
            @Override
            public String getName() {
                return username;
            }
        } ;
    }
    private String getRandom(String[] array){
        return array[random(array.length)];
    }

    private int random(int n){
        Random random = new Random();
        return random.nextInt(n);
    }
}

@Controller
class QuestionController {
    public QuestionController(){
        System.out.println("SSSSSSSSSSSSSSS QuestionController");
    }
    @MessageMapping("/question")
    public String processQuestion(String question, Principal principal){
        return question.toUpperCase() + " by " + principal.getName();

    }
}
