package com.mxk.transfer;

import com.mxk.bean.AppInstance;
import com.mxk.pojo.vo.InstanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 */
@Mapper
public interface InstanceVOTransfer {

    InstanceVOTransfer INSTANCE = Mappers.getMapper(InstanceVOTransfer.class);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(com.mxk.utils.DateUtils.formatToYYYYMMDDHHmmss(appInstance.getCreatedTime()))")
    })
    InstanceVO mapToVO(AppInstance appInstance);

    List<InstanceVO> mapToVOS(List<AppInstance> appInstances);
}
