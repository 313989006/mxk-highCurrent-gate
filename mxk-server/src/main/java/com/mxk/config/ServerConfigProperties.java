package com.mxk.config;

import com.mxk.constants.LoadBalanceConstants;
import com.mxk.constants.MxkExceptionEnum;
import com.mxk.exception.MxkException;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 */
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "mxk.gate")
public class ServerConfigProperties implements InitializingBean {
    /**
     * 负载均衡算法，默认轮询
     */
    private String loadBalance = LoadBalanceConstants.ROUND;
    /**
     * 网关超时时间，默认3s
     */
    private Long timeOutMillis = 3000L;
    /**
     * 缓存刷新间隔，默认10s
     */
    private Long cacheRefreshInterval = 10L;

    private Integer webSocketPort;

    public Integer getWebSocketPort() {
        return webSocketPort;
    }

    public void setWebSocketPort(Integer webSocketPort) {
        this.webSocketPort = webSocketPort;
    }

    public Long getCacheRefreshInterval() {
        return cacheRefreshInterval;
    }

    public void setCacheRefreshInterval(Long cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    public Long getTimeOutMillis() {
        return timeOutMillis;
    }

    public void setTimeOutMillis(Long timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
    }

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.webSocketPort == null || this.webSocketPort <= 0) {
            throw new MxkException(MxkExceptionEnum.CONFIG_ERROR);
        }
    }

}
