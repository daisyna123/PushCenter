package com.netty.handler;
/**
 * Created by administrator on 2017-05-10.
 */

import com.utils.WsUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @DESCRIPTION 建立握手连接
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
public class MyInBoundHandler extends SimpleChannelInboundHandler<Object> {
    public static Map<String,ChannelHandlerContext>  clientChannelMap;
    private WebSocketServerHandshaker handshaker;//记录客户端信息
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
       if(msg instanceof FullHttpRequest){
           //如果是http请求
           handleHttpRequest(channelHandlerContext,(FullHttpRequest) msg);
       }
    }
    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req){
        if ("websocket".equals(req.headers().get("Upgrade"))) {//websocket请求
            // 获取客户端请求uri
            String uri = req.uri();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
            // 获取客户端请求参数
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            // 获取客户端商户号
            String mcId = "";
            if(parameters.get("mcid") != null){
                mcId = parameters.get("mcid").get(0);
            }
            // 记录客户端信息
            handshaker = WsUtils.getHander(req);
            if (handshaker == null) {
                //版本不支持
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), req);
                // 握手成功之后,业务逻辑
                if (channelFuture.isSuccess()) {
                    // 记录客户端信息
                    clientChannelMap.put(mcId,ctx);
                    ChannelFuture future = ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端返回内容---webSocket连接数量"+clientChannelMap.size()));
                    if(future.isSuccess()){//服务器向客户端返回内容成功
                        System.out.println("服务器向客户端返回内容成功");
                    }else {
                        System.out.println("服务器向客户端返回内容失败");
                    }
                }
            }
        }
        //非webSocket请求暂不考虑
//        else{
//            //http请求
//             //获取请求参数
//            Map<String, String> parmMap = getRequestParams(req);
//            System.out.println("http请求参数"+parmMap);
//            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端返回内容---webSocket请求"));
//        }
    }
    private static Map<String, String> getRequestParams(FullHttpRequest req) {
        Map<String, String> requestParams = new HashMap<String, String>();
        // 处理POST请求
        if (req.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : postData) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        getUriPar(req.uri(), requestParams);
        return requestParams;
    }
    private static void getUriPar(String uri, Map<String, String> requestParams) {
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parame = decoder.parameters();
        Iterator<Map.Entry<String, List<String>>> iterator = parame.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            requestParams.put(next.getKey(), next.getValue().get(0));
        }
    }

    
}
