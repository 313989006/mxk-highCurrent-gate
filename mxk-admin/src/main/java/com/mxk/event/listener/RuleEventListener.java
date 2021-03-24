package com.mxk.event.listener;

import com.mxk.constants.OperationTypeEnum;
import com.mxk.event.RuleAddEvent;
import com.mxk.event.RuleDeleteEvent;
import com.mxk.pojo.dto.AppRuleDTO;
import com.mxk.pojo.dto.RouteRuleOperationDTO;
import com.mxk.sync.WebsocketSyncCacheClient;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class RuleEventListener {

    @Autowired
    private WebsocketSyncCacheClient client;

    @EventListener
    public void onAdd(RuleAddEvent ruleAddEvent) {
        RouteRuleOperationDTO operationDTO = new RouteRuleOperationDTO(OperationTypeEnum.INSERT, Lists.newArrayList(ruleAddEvent.getAppRuleDTO()));
        client.send(operationDTO);
    }

    @EventListener
    public void onDelete(RuleDeleteEvent ruleDeleteEvent) {
        List<AppRuleDTO> list = Lists.newArrayList(ruleDeleteEvent.getAppRuleDTO());
        RouteRuleOperationDTO operationDTO = new RouteRuleOperationDTO(OperationTypeEnum.DELETE, list);
        client.send(operationDTO);
    }
}
