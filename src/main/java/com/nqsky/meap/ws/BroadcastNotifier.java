package com.nqsky.meap.ws;

import java.util.Collection;
import java.util.List;

public interface BroadcastNotifier {
    void notify(Collection<String> uids, String notificationCode);
}
