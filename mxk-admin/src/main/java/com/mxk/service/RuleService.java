package com.mxk.service;

import com.mxk.pojo.ChangeStatusDTO;
import com.mxk.pojo.RuleDTO;
import com.mxk.pojo.dto.AppRuleDTO;
import com.mxk.pojo.vo.RuleVO;

import java.util.List;

/**
 *
 */
public interface RuleService {

    List<AppRuleDTO> getEnabledRule();

    void add(RuleDTO ruleDTO);

    void delete(Integer id);

    List<RuleVO> queryList(String appName);

    void changeStatus(ChangeStatusDTO statusDTO);
}
