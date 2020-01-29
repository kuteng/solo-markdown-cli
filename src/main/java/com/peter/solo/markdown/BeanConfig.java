package com.peter.solo.markdown;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration("beanConfig")
@ImportResource(locations={"classpath:bean.xml"})
public class BeanConfig {
}
