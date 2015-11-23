package com.li.slave.communication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lihongli on 15/11/23.
 */
@Configuration
@Import({SlaveBeanConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {

}
