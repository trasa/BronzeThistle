package com.meancat.bronzethistle.shared;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
//@EnableCaching  // not yet
//@EnableAspectJAutoProxy // not yet
//@ImportResource("classpath*:simplesm-context.xml") // not yet
@ComponentScan(basePackageClasses = { SharedConfiguration.class})
public class SharedConfiguration {

}
