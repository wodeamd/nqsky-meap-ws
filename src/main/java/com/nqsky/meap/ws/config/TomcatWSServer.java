package com.nqsky.meap.ws.config;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocketendpoint")
public class TomcatWSServer {

	private static Set<Session> userSocket = new HashSet() ;
	
	@OnOpen
	public void onOpen( Session session){
		try{
			System.out.println("Open Connection ...");
			userSocket.add(session);
			session.getBasicRemote().sendText("Open response.");
		} catch (Exception e){
			 e.printStackTrace();
		}

	}
	
	@OnClose
	public void onClose(){
		System.out.println("Close Connection ...");
	}
	
	@OnMessage
	public void onMessage(Session session, String message)throws Exception{
		System.out.println("Message from the client: " + message);
		 try {
//			 for(Session session:userSocket){
				 if(session.isOpen()){
					 session.getBasicRemote().sendText("Echo --- " + message);
					 System.out.println("Echo from the server : " + message);
				 } else{
					 System.out.println("Session " + session + " is closed.");
				 }

//			 }
		 }catch (Exception e){
		 	e.printStackTrace();
		 }


//		return echoMsg;
	}

	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}

}
