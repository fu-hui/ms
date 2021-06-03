package com.github.ms;

import com.github.ms.stomp.StompServer;

public class MsApplication {
    public static void main(String[] args) {
        new StompServer().run();
    }
}
