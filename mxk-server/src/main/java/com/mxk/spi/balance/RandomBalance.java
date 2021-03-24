package com.mxk.spi.balance;

import com.mxk.annotation.LoadBalanceAno;
import com.mxk.constants.LoadBalanceConstants;
import com.mxk.pojo.dto.ServiceInstance;
import com.mxk.spi.LoadBalance;

import java.util.Random;

/**
 * 随机策略
 */
@LoadBalanceAno(LoadBalanceConstants.RANDOM)
public class RandomBalance implements LoadBalance {

    private static Random random = new Random();

    @Override
    public ServiceInstance chooseOne(java.util.List<ServiceInstance> instances) {
        return instances.get(random.nextInt(instances.size()));
    }
}
