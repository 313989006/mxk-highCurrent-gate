package com.mxk.transfer;

import com.mxk.bean.AppInstance;
import com.mxk.pojo.dto.ServiceInstance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 */
@Mapper
public interface AppInstanceTransfer {

    AppInstanceTransfer INSTANCE = Mappers.getMapper(AppInstanceTransfer.class);

    ServiceInstance mapToService(AppInstance appInstance);

    List<ServiceInstance> mapToServiceList(List<AppInstance> appInstances);
}
