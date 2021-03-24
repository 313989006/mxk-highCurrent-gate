package com.mxk.transfer;

import com.mxk.bean.RouteRule;
import com.mxk.pojo.dto.AppRuleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 */
@Mapper
public interface AppRuleVOTransfer {

    AppRuleVOTransfer INSTANCE = Mappers.getMapper(AppRuleVOTransfer.class);

    AppRuleDTO mapToVO(RouteRule routeRule);

    List<AppRuleDTO> mapToVOList(List<RouteRule> routeRules);
}
