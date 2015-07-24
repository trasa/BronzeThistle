package com.meancat.bronzethistle.roomserver;

import com.meancat.bronzethistle.shared.SharedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = { RoomServerConfiguration.class })
@Import( { SharedConfiguration.class })
public class RoomServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RoomServerConfiguration.class);


}
