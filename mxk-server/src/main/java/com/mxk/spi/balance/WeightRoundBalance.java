package com.mxk.spi.balance;


import com.mxk.annotation.LoadBalanceAno;
import com.mxk.constants.LoadBalanceConstants;
import com.mxk.pojo.dto.ServiceInstance;
import com.mxk.spi.LoadBalance;

/**
 *  加权轮询
 */
@LoadBalanceAno(LoadBalanceConstants.WEIGHT_ROUND)
public class WeightRoundBalance implements LoadBalance {

    private volatile int index;

    @Override
    public synchronized ServiceInstance chooseOne(java.util.List<ServiceInstance> instances) {
        int allWeight = instances.stream().mapToInt(ServiceInstance::getWeight).sum();
        int number = (index++) % allWeight;
        for (ServiceInstance instance : instances) {
            if (instance.getWeight() > number) {
                return instance;
            }
            number -= instance.getWeight();
        }
        return null;
    }
}
