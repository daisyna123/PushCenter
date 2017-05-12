package com.utils;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.handler.MyInBoundHandler;
import com.netty.server.MyServer;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @DESCRIPTION spring启动后自启动netty服务
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
@Component
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${netty.port}")
    private String port;
    @Resource
    private MyServer server;
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            /**
             * 初始化商户websocket连接Map
             * key:商户id
             * value:商户websocket会话
             */
            MyInBoundHandler.clientChannelMap = new HashMap<String, ChannelHandlerContext>();
            server.bind(Integer.parseInt(port));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
