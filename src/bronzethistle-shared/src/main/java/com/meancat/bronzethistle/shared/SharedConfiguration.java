package com.meancat.bronzethistle.shared;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy
@ImportResource("classpath*:simplesm-context.xml")
@ComponentScan(basePackageClasses = { SharedConfiguration.class})
public class SharedConfiguration {

}
