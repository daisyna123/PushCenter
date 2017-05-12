package com.netty.handler;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.heart.HeartBeat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @DESCRIPTION 初始化handler
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
@Service
public class MyChannelHandler extends ChannelInitializer<SocketChannel>{
    // 读超时
    private static final int READ_IDEL_TIME_OUT = 5;
    // 写超时
    private static final int WRITE_IDEL_TIME_OUT = 3;
    // 所有超时
    private static final int ALL_IDEL_TIME_OUT = 5;
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //将消息进行解码
        pipeline.addLast(new HttpServerCodec());
        //HttpObjectAggregator 将多个消息转换为单一的一个FullHttpRequest
        pipeline.addLast(new HttpObjectAggregator(65536));
        //IdleStateHandler 心跳检测
        pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
                WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast(new WriteTimeoutHandler(3, TimeUnit.SECONDS));
        pipeline.addLast(new HeartBeat());
        pipeline.addLast(new MyInBoundHandler());
    }
}
