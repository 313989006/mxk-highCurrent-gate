package com.mxk;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 客户端自动配置解析
 */
@Configuration
@Import(value = {AutoRegisterListener.class})
@EnableConfigurationProperties(value = {ClientConfigProperties.class})
public class ClientAutoConfigure {



}
