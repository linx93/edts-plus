package com.phadata.etdsplus;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.phadata.etdsplus.mq.InitMQInfo;
import com.phadata.etdsplus.service.EtdsService;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

/**
 * @description: 通过rabbitMQ的管理界面获取队列测试
 * @author: linx
 * @create: 2021-11-22 17:22
 */
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private InitMQInfo initMQInfo;
    @Autowired
    private EtdsService etdsService;

    /**
     * 通过控制台提供的web api获取所以队列
     *
     * @throws IOException
     */
    @Test
    void queuesTest() throws IOException {
        String apiMessage = getApiMessage();
        System.out.println(apiMessage);
    }

    public String getApiMessage() throws IOException {
        //发送一个GET请求
        HttpURLConnection httpConn = null;
        BufferedReader in = null;
        String urlQueues = "http://192.168.1.107:15672/api/queues";
        URL url = new URL(urlQueues);
        httpConn = (HttpURLConnection) url.openConnection();
        //设置用户名密码
        String username = "fecred";
        String password = "123456";
        String auth = username + ":" + password;
        Base64.Encoder encoder = Base64.getEncoder();
        String encoding = encoder.encodeToString(auth.getBytes());
        httpConn.setDoOutput(true);
        httpConn.setRequestProperty("Authorization", "Basic " + encoding);
        // 建立实际的连接
        httpConn.connect();
        //读取响应
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            StringBuilder content = new StringBuilder();
            String tempStr = "";
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            while ((tempStr = in.readLine()) != null) {
                content.append(tempStr);
            }
            in.close();
            httpConn.disconnect();
            return content.toString();
        } else {
            httpConn.disconnect();
            return "";
        }
    }


    /**
     * 测试初始化队列和绑定关系
     */
    @Test
    void initMQInfoTest() {
        initMQInfo.initMQInfo(etdsService);
    }

    /**
     * 测试移除队列和交换机的绑定关系
     */
    @Test
    void removeBinding() {
        initMQInfo.removeBinding(etdsService);
    }
}
