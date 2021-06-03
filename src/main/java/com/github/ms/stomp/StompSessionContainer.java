package com.github.ms.stomp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StompSessionContainer {
    private static final Logger logger = LoggerFactory.getLogger(StompSessionContainer.class);
    private static final Map<String, StompSession> userSessions = new ConcurrentHashMap<>();
    private static final Map<String, String> usernames = new ConcurrentHashMap<>();

    public static void addSession(StompSession stompSession) {
        userSessions.putIfAbsent(stompSession.getSessionId(), stompSession);
        usernames.putIfAbsent(stompSession.getUsername(), stompSession.getSessionId());
        logger.info("stompSession add success: {}", stompSession);
    }

    public static void removeSession(String sessionId) {
        StompSession stompSession = userSessions.get(sessionId);
        userSessions.remove(sessionId);
        usernames.remove(stompSession.getUsername());
    }

    public static StompSession getStompSessionBySessionId(String sessionId) {
        return userSessions.get(sessionId);
    }

    public static StompSession getStompSessionByUsername(String username) {
        String key = usernames.get(username);
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return userSessions.get(key);
    }
}


