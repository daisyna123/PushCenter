package com.netty.heart;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.handler.MyInBoundHandler;
import com.utils.TCSL_UTIL_Resource;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.spi.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

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
        String hotelCode = "";
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                System.out.println("--- Write Idle ---");
                /**
                 * 根据channel 取得对应的酒店编码
                 */
                Set<String> keySet = MyInBoundHandler.clientChannelMap.keySet();
                Iterator<String> keyIte = keySet.iterator();
                while (keyIte.hasNext()){
                    String key = keyIte.next();
                    ChannelHandlerContext channelCtx =  MyInBoundHandler.clientChannelMap.get(key);
                   String channelId = channelCtx.channel().id().toString();
                   String currentChannelId = ctx.channel().id().toString();
                    if(channelId.equals(currentChannelId)){
                        hotelCode = key;
                    }
                }
                ChannelFuture future =  ctx.channel().writeAndFlush(new TextWebSocketFrame("Boom Boom Boom... ...\r\n"));
                if(future.isSuccess()){//向客户端发送心跳成功
                    TCSL_UTIL_Resource.heartBeatFailMap.put(hotelCode,0);
                    System.out.println("向客户端发送心跳成功");
                }else{
                    Integer failNum = TCSL_UTIL_Resource.heartBeatFailMap.get(hotelCode);
                    if(failNum != null){
                        if(failNum == 10){
                            TCSL_UTIL_Resource.heartBeatFailMap.put(hotelCode,0);
                            MyInBoundHandler.clientChannelMap.get(hotelCode).close();
                        }else {
                            failNum = failNum + 1;
                            TCSL_UTIL_Resource.heartBeatFailMap.put(hotelCode,failNum);
                        }
                    }else{
                        ctx.close();
                    }
                    System.out.println("向客户端发送心跳失败");
                }
            }

        }
    }
}
