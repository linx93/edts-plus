package com.phadata.etdsplus.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yd
 * @desc 获取ip地址工具
 * @time 11/15/20 8:10 PM
 * @since
 */
@Slf4j
public class IpUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        String unknown = "unknown";
        String localhost = "127.0.0.1";
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (localhost.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("获取ip 地址 异常：{}", e.getMessage());
                    }
                    assert inet != null;
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            int ipLen = 15;
            if (ipAddress != null && ipAddress.length() > ipLen) {
                // = 15
                String indexOf = ",";
                if (ipAddress.indexOf(indexOf) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }

}
