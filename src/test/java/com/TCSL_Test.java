package com;/**
 * Created by administrator on 2017-05-10.
 */

import com.netty.server.MyServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @DESCRIPTION 测试
 * @AUTHER administrator zhangna
 * @create 2017-05-10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-web.xml"})
public class TCSL_Test {
    @Resource
    private MyServer server;
    @Test
    public void test() {
        try {
            server.bind(1235);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
