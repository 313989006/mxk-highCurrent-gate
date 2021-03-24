package com.mxk.transfer;

import com.mxk.bean.RouteRule;
import com.mxk.pojo.vo.RuleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 */
@Mapper
public interface RuleVOTransfer {

    RuleVOTransfer INSTANCE = Mappers.getMapper(RuleVOTransfer.class);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(com.mxk.utils.DateUtils.formatToYYYYMMDDHHmmss(rule.getCreatedTime()))")
    })
    RuleVO mapToVO(RouteRule rule);

    List<RuleVO> mapToVOList(List<RouteRule> rules);
}
