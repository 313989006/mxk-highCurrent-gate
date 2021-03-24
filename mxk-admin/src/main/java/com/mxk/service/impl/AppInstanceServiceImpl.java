package com.mxk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mxk.bean.App;
import com.mxk.bean.AppInstance;
import com.mxk.mapper.AppInstanceMapper;
import com.mxk.mapper.AppMapper;
import com.mxk.pojo.UpdateWeightDTO;
import com.mxk.pojo.vo.InstanceVO;
import com.mxk.service.AppInstanceService;
import com.mxk.transfer.InstanceVOTransfer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class AppInstanceServiceImpl implements AppInstanceService {

    @Resource
    private AppInstanceMapper instanceMapper;

    @Resource
    private AppMapper appMapper;

    @Override
    public List<InstanceVO> queryList(Integer appId) {
        App app = appMapper.selectById(appId);
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, appId);
        List<AppInstance> instanceList = instanceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(instanceList)) {
            return Lists.newArrayList();
        }
        List<InstanceVO> voList = InstanceVOTransfer.INSTANCE.mapToVOS(instanceList);
        voList.forEach(vo -> vo.setAppName(app.getAppName()));
        return voList;
    }

    @Override
    public void updateWeight(UpdateWeightDTO updateWeightDTO) {
        AppInstance appInstance = new AppInstance();
        appInstance.setId(updateWeightDTO.getId());
        appInstance.setWeight(updateWeightDTO.getWeight());
        instanceMapper.updateById(appInstance);
    }
}
