package com.github.ms.service;

import com.github.ms.service.bean.MsRecord;
import com.github.ms.stomp.StompSession;
import com.github.ms.stomp.StompSessionContainer;
import com.google.gson.Gson;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.stomp.Command;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.ServerFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MsHandler {
    private static final Logger logger = LoggerFactory.getLogger(MsHandler.class);
    private static final MsHandler instance = new MsHandler();
    private static final Gson gson = new Gson();
    private MsHandler() {
    }
    public static MsHandler getInstance() {
        return instance;
    }

    public void handle(StompSession stompSession, ServerFrame serverFrame) {
        MsRecord msRecord = decodeMsRecord(serverFrame);

        List<String> toList = msRecord.getToList();
        if (toList == null || toList.size() == 0) {
            return;
        }

        logger.info("to list is: {}", gson.toJson(toList));
        for (String username : toList) {
            StompSession session = StompSessionContainer.getStompSessionByUsername(username);
            if (session == null) {
                logger.warn("user {} is offline", username);
                continue;
            }

            Frame msgFrame = new Frame(Command.MESSAGE, new HashMap<>(), Buffer.buffer(gson.toJson(msRecord)));
            session.getStompServerConnection().write(msgFrame);
        }
    }

    private MsRecord decodeMsRecord(ServerFrame serverFrame) {
        MsRecord msRecord = gson.fromJson(serverFrame.frame().getBody().toString(), MsRecord.class);
        msRecord.setId(UUID.randomUUID().toString());
        msRecord.setTimestamp(System.currentTimeMillis());
        return msRecord;
    }
}
