package com.mqunar.necro.agent.instrumentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface SkipTrace {
}
