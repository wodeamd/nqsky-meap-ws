package com.nqsky.meap.ws.service;

import com.nqsky.meap.api.notify.IBroadCastService;
import com.nqsky.meap.ws.BroadcastNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.nqsky.meap.ws.constant.Constant.NOTIFY_CODE_BROADCAST;

@Service
public class NotificationDriver implements IBroadCastService {
//    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//    public NotificationDriver(){
//        final List<String> uidList = new LinkedList<>();
//        uidList.add("1");
//        uidList.add("2");
//        uidList.add("3");
//        uidList.add("4");
//        uidList.add("5");
//        uidList.add("6");
//        uidList.add("7");
//        uidList.add("8");
//        uidList.add("9");
//        uidList.add("10");
//        executorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                broadcastNotifier.notify(uidList, NOTIFY_CODE_BROADCAST);
//            }
//        },10, 10, TimeUnit.SECONDS);
//    }


    @Autowired
    private BroadcastNotifier broadcastNotifier;
    

    @Override
    public void broadCast(Collection<String> uidList) {
        System.out.println("Receive broadcast request, size=" + uidList.size());
        broadcastNotifier.notify(uidList, NOTIFY_CODE_BROADCAST);
    }
}
