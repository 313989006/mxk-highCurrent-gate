package com.mxk.spi;


import com.mxk.pojo.dto.ServiceInstance;

/**
 *
 */
public interface LoadBalance {
    /**
     * Select an instance based on the load balancing algorithm
     * @param instances
     * @return
     */
    ServiceInstance chooseOne(java.util.List<ServiceInstance> instances);
}
