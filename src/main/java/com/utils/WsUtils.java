package com.utils;/**
 * Created by administrator on 2017-05-10.
 */

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * @DESCRIPTION 握手工具类
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
public class WsUtils {
    static {
        wsFactory = new WebSocketServerHandshakerFactory("", null, true);
    }
    private static WebSocketServerHandshakerFactory wsFactory;
    public static WebSocketServerHandshaker getHander(HttpRequest req) {
        return wsFactory.newHandshaker(req);
    }
}
