package com.nqsky.meap.ws.service;

import com.nqsky.meap.core.Configs;
import com.nqsky.meap.core.tool.Executors;
import com.nqsky.meap.ws.BroadcastNotifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static org.springframework.web.socket.CloseStatus.BAD_DATA;


@Component
public class NotificationService extends TextWebSocketHandler implements BroadcastNotifier {
    public static int NOTIFY_BATCH_SIZE = Configs.getIntProperty(
            "notify_batch_size", 100);

    private ConcurrentHashMap<String, WebSocketSession>  sessionMap = new ConcurrentHashMap<>();

    private ExecutorService executor = Executors.newThreadPoolExecutor("SystemBroadcasterService",20,50, 1000);
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String uid = getUid(session);
        if(uid == null){
            session.close(BAD_DATA);
            return;
        }

        String uniqeID = session.getId();
        System.out.println("Connection established with uid:" + uid + "  uniqeID:" + uniqeID );

        WebSocketSession oldSession = sessionMap.put(uid, session);
        if(oldSession != null){
            System.out.println("Close privious session:" + oldSession.getId());
            oldSession.close();
        }
    }

    private String getUid(WebSocketSession session){
        try{
            String query = session.getUri().getQuery();
            return query.substring(query.indexOf("=") + 1);
        } catch (Exception e){
            System.out.println("Fail to get uid for session:" + session);
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uid = getUid(session);
        if(uid == null){
            System.out.println("There is no uid for session id:" + session.getId());
            return;
        }

        if( sessionMap.remove(uid, session)){
            System.out.println("Remove session session:" +  session + " for uid:" + uid);
        }

    }

    @Override
    public void notify(Collection<String> uidList, String notificationCode) {
        if(uidList.size() <= NOTIFY_BATCH_SIZE){
            doNotify(uidList,notificationCode);
        }else{
            List<String> batch = new LinkedList<>();
            for(String uid:uidList){
                batch.add(uid);
                if(batch.size() == NOTIFY_BATCH_SIZE){
                    doNotify(batch, notificationCode);
                    batch = new LinkedList<>();
                }
            }

            if(!batch.isEmpty()){
                doNotify(batch, notificationCode);
            }
        }
    }

    public void doNotify(final Collection<String> uidList, final String notificationCode) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                for(String uid:uidList){
                    WebSocketSession session = sessionMap.get(uid);
                    if(session == null){
                        System.out.println("Session not exist for uid:" + uid);
                    } else{
                        if(session.isOpen()){
                            try{
                                System.out.println("Will send to uid:" + uid + "  notification:" + notificationCode);
                                session.sendMessage(new TextMessage(notificationCode.getBytes()));
                            }catch (IOException e){
                                System.out.println("Error when sending notification " + notificationCode + " to session:" + session + " for uid:" + uid);
                                e.printStackTrace();
                                try{
                                    session.close();
                                }catch (IOException ee){
                                    //ignore
                                }

                            }

                        }else{
                            System.out.println("Session:" + session + " is closed for uid:" + uid);
                            if(sessionMap.remove(uid, session)){
                                System.out.println("Remove session:" +  session + " for uid:" + uid);
                            }

                        }
                    }
                }
            }
        });
    }
}
