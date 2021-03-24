package com.mxk.controller;

import com.mxk.pojo.ChangeStatusDTO;
import com.mxk.pojo.RuleDTO;
import com.mxk.pojo.dto.AppRuleDTO;
import com.mxk.pojo.vo.Result;
import com.mxk.pojo.vo.RuleVO;
import com.mxk.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/rule")
public class RuleController {

    @Resource
    private RuleService ruleService;

    /**
     * add new route rule
     *
     * @param ruleDTO
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public Result add(@Validated RuleDTO ruleDTO){
        ruleService.add(ruleDTO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        ruleService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public String list(ModelMap map, @RequestParam(name = "appName", required = false) String appName) {
        List<RuleVO> ruleVOS = ruleService.queryList(appName);
        map.put("ruleVOS", ruleVOS);
        map.put("appName", appName);
        return "rule";
    }

    @ResponseBody
    @PutMapping("/status")
    public Result changeStatus(@RequestBody ChangeStatusDTO statusDTO) {
        ruleService.changeStatus(statusDTO);
        return Result.success();
    }
}
