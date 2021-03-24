package com.mxk.service;

import com.mxk.pojo.UpdateWeightDTO;
import com.mxk.pojo.vo.InstanceVO;

import java.util.List;

/**
 *
 */
public interface AppInstanceService {
    /**
     * query instances by appId
     * @param appId
     * @return
     */
    List<InstanceVO> queryList(Integer appId);

    void updateWeight(UpdateWeightDTO updateWeightDTO);
}
