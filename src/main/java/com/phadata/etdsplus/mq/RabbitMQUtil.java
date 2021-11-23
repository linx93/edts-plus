package com.phadata.etdsplus.mq;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

/**
 * @description: 通过web api 获取mq相关信息的工具类
 * @author: xionglin
 * @create: 2021-11-23 09:55
 */
@Component
public class RabbitMQUtil {
    private final EtdsService etdsService;

    public RabbitMQUtil(EtdsService etdsService) {
        this.etdsService = etdsService;
    }

    private String queueList() {
        //发送一个GET请求
        HttpURLConnection httpConn = null;
        BufferedReader in = null;
        String urlQueues = "http://192.168.1.107:15672/api/queues";

        //设置用户名密码
        String username = "fecred";
        String password = "123456";
        String auth = username + ":" + password;
        Base64.Encoder encoder = Base64.getEncoder();
        String encoding = encoder.encodeToString(auth.getBytes());
        try {
            URL url = new URL(urlQueues);
            httpConn = (HttpURLConnection) url.openConnection();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
