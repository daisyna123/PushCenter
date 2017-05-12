package com.netty.heart;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.handler.MyInBoundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.spi.LoggerFactory;

/**
 * @DESCRIPTION 心跳
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
public class HeartBeat extends ChannelHandlerAdapter {
    //首先添加了idleStateHandler用于监听链接idle，如果连接到达idle时间，这个handler会触发idleEvent，之后通过重写userEventTriggered方法，完成idle事件的处理。
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                System.out.println("--- Write Idle ---");
                ChannelFuture future =  ctx.channel().writeAndFlush(new TextWebSocketFrame("Boom Boom Boom... ...\r\n"));
            }
        }

    }
}
