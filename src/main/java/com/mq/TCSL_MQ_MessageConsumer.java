package com.mq;/**
 * Created by administrator on 2017-05-10.
 */

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;

//import java.util.logging.Logger;

/**
 * @DESCRIPTION mq消息接收类
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
public class TCSL_MQ_MessageConsumer implements MessageListener{
    private Logger logger = Logger.getLogger(TCSL_MQ_MessageConsumer.class);

    public void onMessage(Message message) {
        byte bytes[] = message.getBody();
        String isoString = null;
        try {
            isoString = new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("--------接收到data--------"+isoString);
        logger.info("--------接收到data--------"+isoString);
    }
}
