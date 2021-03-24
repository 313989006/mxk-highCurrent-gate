package com.mxk.mapper;

import com.mxk.bean.AppPlugin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mxk.pojo.AppPluginDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface AppPluginMapper extends BaseMapper<AppPlugin> {

    List<AppPluginDTO> queryEnabledPlugins(@Param("appIds") List<Integer> appIds);
}
