package com.mxk.transfer;

import com.mxk.bean.App;
import com.mxk.pojo.vo.AppVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 */
@Mapper
public interface AppVOTransfer {

    AppVOTransfer INSTANCE = Mappers.getMapper(AppVOTransfer.class);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(com.mxk.utils.DateUtils.formatToYYYYMMDDHHmmss(app.getCreatedTime()))")
    })
    AppVO mapToVO(App app);

    List<AppVO> mapToVOList(List<App> appList);
}
