package com.mxk.service;

import com.mxk.pojo.ChangeStatusDTO;
import com.mxk.pojo.vo.AppVO;
import com.mxk.pojo.dto.AppInfoDTO;
import com.mxk.pojo.dto.RegisterAppDTO;
import com.mxk.pojo.dto.UnregisterAppDTO;

import java.util.List;

/**
 *
 */
public interface AppService {

    /**
     * register app
     *
     * @param registerAppDTO
     */
    void register(RegisterAppDTO registerAppDTO);

    /**
     * unregister app instance
     *
     * @param unregisterAppDTO
     */
    void unregister(UnregisterAppDTO unregisterAppDTO);

    /**
     * get app infos by appNames
     * @param appNames
     * @return
     */
    List<AppInfoDTO> getAppInfos(List<String> appNames);

    List<AppVO> getList();

    void updateEnabled(ChangeStatusDTO statusDTO);

    void delete(Integer id);
}
