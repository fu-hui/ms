package com.github.ms.stomp;

import com.github.ms.service.MsHandler;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.impl.DefaultStompHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StompServer {
    private static final Logger logger = LoggerFactory.getLogger(MsHandler.class);

    private static final MsHandler msHandler = MsHandler.getInstance();
    public void run() {
        Vertx vertx = Vertx.vertx();
        io.vertx.ext.stomp.StompServer stompServer = io.vertx.ext.stomp.StompServer.create(vertx);
        StompServerHandler stompServerHandler = new DefaultStompHandler(vertx);
        stompServer.handler(stompServerHandler);
        stompServerHandler.closeHandler(this::closeHandle);
        stompServerHandler.receivedFrameHandler(this::frameHandle);
        Future<io.vertx.ext.stomp.StompServer> listen = stompServer.listen(18080);
        listen.onSuccess(server -> {
            logger.info("server init success");
        }).onFailure(throwable -> {
            logger.error("server init failed");
            throwable.printStackTrace();
        });
    }

    private void frameHandle(ServerFrame serverFrame) {
        switch (serverFrame.frame().getCommand()) {
            case PING:
                return;
            case CONNECT:
                connectHandle(serverFrame);
                break;
            case DISCONNECT:
                disConnectHandle(serverFrame);
                break;
            case SEND:
                sendHandle(serverFrame);
                break;
            default:
                logger.warn("not use cmd {}", serverFrame.frame().getCommand());
                break;
        }
    }

    private void closeHandle(StompServerConnection stompServerConnection) {
        logger.info("stomp session {} will close", stompServerConnection.session());
        StompSession stompSession = StompSessionContainer.getStompSessionBySessionId(stompServerConnection.session());
        if (stompSession == null) {
            return;
        }

        logger.info("user {} will logout", stompSession.getUsername());
        StompSessionContainer.removeSession(stompServerConnection.session());
    }

    private void connectHandle(ServerFrame serverFrame) {
        String userInfo = serverFrame.frame().getHeader("login");
        String[] split = userInfo.split(":");
        String username = split[0];
        String password = split[1];
        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            logger.error("user {}:{} login failed", username, password);
            return;
        }
        logger.info("connectHandler serverFrame.frame().toString() = {}", serverFrame.frame().toString());
        StompSessionContainer.addSession(new StompSession(serverFrame.connection().session(),
                username, serverFrame.connection()));
    }

    private void disConnectHandle(ServerFrame serverFrame) {
        closeHandle(serverFrame.connection());
    }

    private void sendHandle(ServerFrame serverFrame) {
        StompSession stompSession = StompSessionContainer.getStompSessionBySessionId(serverFrame.connection().session());
        logger.info("msg destination is: {}", serverFrame.frame().getDestination());
        if (serverFrame.frame().getDestination().startsWith("msg")) {
            msHandler.handle(stompSession, serverFrame);
        }
    }
}
