package com.mxk.cache;

import com.mxk.exception.MxkException;
import com.mxk.pojo.dto.AppRuleDTO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 路由规则缓存
 */
public class RouteRuleCache {

    private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RouteRuleCache.class);
    /**
     * route rule config cache map
     * key: appName
     */
    private static final Map<String, CopyOnWriteArrayList<AppRuleDTO>> ROUTE_RULE_MAP = new ConcurrentHashMap<>();

    /**
     * add rule to cache map
     *
     * @param map
     */
    public static void add(Map<String, List<AppRuleDTO>> map) {
        map.forEach((key, value) -> {
            ROUTE_RULE_MAP.put(key, new CopyOnWriteArrayList(value));
        });
    }

    /**
     * remove rule from cache map
     *
     * @param map
     */
    public static void remove(Map<String, List<AppRuleDTO>> map) {
        for (Map.Entry<String, List<AppRuleDTO>> entry : map.entrySet()) {
            String appName = entry.getKey();
            List<Integer> ruleIds = entry.getValue().stream().map(AppRuleDTO::getId).collect(Collectors.toList());
            CopyOnWriteArrayList<AppRuleDTO> ruleDTOS = ROUTE_RULE_MAP.getOrDefault(appName, new CopyOnWriteArrayList<>());
            ruleDTOS.removeIf(r -> ruleIds.contains(r.getId()));
            if (CollectionUtils.isEmpty(ruleDTOS)) {
                // remove all
                ROUTE_RULE_MAP.remove(appName);
            } else {
                // remove some of them
                ROUTE_RULE_MAP.put(appName, ruleDTOS);
            }
        }
    }


    /**
     * get rules by appName
     *
     * @param appName
     * @return
     */
    public static List<AppRuleDTO> getRules(String appName) {
        return Optional.ofNullable(ROUTE_RULE_MAP.get(appName))
                .orElseThrow(() -> new MxkException("please config route rule in mxk-admin first!"));
    }

}
