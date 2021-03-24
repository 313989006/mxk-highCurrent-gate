package com.mxk.cache;

import com.mxk.annotation.LoadBalanceAno;
import com.mxk.exception.MxkException;
import com.mxk.spi.LoadBalance;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载均衡策略工厂类
 */
public final class LoadBalanceFactory {

    /**
     * key: appName:version
     */
    private static final java.util.Map<String, LoadBalance> LOAD_BALANCE_MAP = new ConcurrentHashMap<>();

    private LoadBalanceFactory(){

    }

    /**
     * get LoadBalance instance
     * @param name
     * @param appName
     * @param version
     * @return
     */
    public static LoadBalance getInstance(final String name, String appName, String version) {
        String key = appName + ":" + version;
        return LOAD_BALANCE_MAP.computeIfAbsent(key, (k) -> getLoadBalance(name));
    }

    /**
     * use spi to match load balance algorithm by server config
     *
     * @param name
     * @return
     */
    private static LoadBalance getLoadBalance(String name) {
        ServiceLoader<LoadBalance> loader = ServiceLoader.load(LoadBalance.class);
        Iterator<LoadBalance> iterator = loader.iterator();
        while (iterator.hasNext()) {
            LoadBalance loadBalance = iterator.next();
            LoadBalanceAno ano = loadBalance.getClass().getAnnotation(LoadBalanceAno.class);
            Assert.notNull(ano, "load balance name can not be empty!");
            if (name.equals(ano.value())) {
                return loadBalance;
            }
        }
        throw new MxkException("invalid load balance config");
    }
}
