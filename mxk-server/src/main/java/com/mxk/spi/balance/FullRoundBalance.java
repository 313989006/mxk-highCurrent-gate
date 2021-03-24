package com.mxk.spi.balance;


import com.mxk.annotation.LoadBalanceAno;
import com.mxk.constants.LoadBalanceConstants;
import com.mxk.pojo.dto.ServiceInstance;
import com.mxk.spi.LoadBalance;

/**
 * 轮询策略
 */
@LoadBalanceAno(LoadBalanceConstants.ROUND)
public class FullRoundBalance implements LoadBalance {

    private volatile int index;

    @Override
    public synchronized ServiceInstance chooseOne(java.util.List<ServiceInstance> instances) {
        if (index == instances.size()) {
            index = 0;
        }
        return instances.get(index++);
    }
}
