package com.mxk.pojo.dto;


import com.mxk.constants.OperationTypeEnum;

import java.util.List;

/**
 * 路由规则实体类
 */
public class RouteRuleOperationDTO {
    /**
     * {@link OperationTypeEnum}
     */
    private String operationType;

    private List<AppRuleDTO> ruleList;

    public RouteRuleOperationDTO() {
    }

    public RouteRuleOperationDTO(OperationTypeEnum operationTypeEnum, List<AppRuleDTO> ruleList) {
        this.operationType = operationTypeEnum.getCode();
        this.ruleList = ruleList;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public List<AppRuleDTO> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<AppRuleDTO> ruleList) {
        this.ruleList = ruleList;
    }
}
