package com.netty.server;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.handler.MyChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @DESCRIPTION netty server端
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
@Component
public class MyServer {
    @Autowired
    private MyChannelHandler myChannelHandler;
    //绑定端口，启动服务
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();//用于serversocketchannel的eventloop
        EventLoopGroup workerGroup = new NioEventLoopGroup();//用于处理accept到的channel

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup); //设置时间循环对象，前者用来处理accept事件，后者用于处理已经建立的连接的io
            b.channel(NioServerSocketChannel.class);//用它来建立新accept的连接，用于构造serversocketchannel的工厂类
            b.option(ChannelOption.SO_BACKLOG,1024);
            b.childHandler(myChannelHandler);//为accept channel的pipeline预添加的inboundhandler
            System.out.println("server start !!!");
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();


        }finally {
            System.out.println("server stop !!!");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
