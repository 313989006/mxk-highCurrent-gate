package com.mxk.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Ip 操作工具类
 */
public class IpUtil {

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
