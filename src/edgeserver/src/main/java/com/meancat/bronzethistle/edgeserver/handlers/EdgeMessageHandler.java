package com.meancat.bronzethistle.edgeserver.handlers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods tagged with this annotation are indicating that they should be entered into the
 * EdgeMessageHandlerRegistry to deal with various payloads of EdgeMessages.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EdgeMessageHandler {
}
