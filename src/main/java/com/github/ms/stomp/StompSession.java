package com.github.ms.stomp;

import io.vertx.ext.stomp.StompServerConnection;

public class StompSession {
    private final String sessionId;
    private final String username;
    private final StompServerConnection stompServerConnection;

    public StompSession(String sessionId, String username, StompServerConnection stompServerConnection) {
        this.sessionId = sessionId;
        this.username = username;
        this.stompServerConnection = stompServerConnection;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public StompServerConnection getStompServerConnection() {
        return stompServerConnection;
    }

    @Override
    public String toString() {
        return "StompSession{" +
                "sessionId='" + sessionId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
