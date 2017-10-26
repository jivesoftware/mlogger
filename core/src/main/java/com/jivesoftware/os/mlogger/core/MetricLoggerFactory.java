/*
 * Copyright 2013 Jive Software, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jivesoftware.os.mlogger.core;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;

/**
 * Convenient way to create MetricLoggers. This impl will create metric a metric logger that exposes all its metrics via JMX. Added the concept of
 * external loggers. Which means the messages they log relate to external service interactions.
 *
 * @author jonathan
 */
public class MetricLoggerFactory {

    final static public ConcurrentHashMap<String, MetricLogger> serviceLoggers = new ConcurrentHashMap<>();

    /**
     * This is a magic way to avoid providing the class when creating a MetricLogger!
     */
    static public MetricLogger getLogger() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        // index 0 Thread
        // index 1 MetricLoggerFactory
        // index 2 calling class
        return getLogger(elements[2].getClassName());
    }

    static public MetricLogger getLogger(Class<?> _class) {
        return getLogger(_class.getCanonicalName());
    }

    static public MetricLogger getLogger(String name) {
        return getLogger(name, false);
    }

    static public MetricLogger getLogger(boolean logsExternalInteractions) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        // index 0 Thread
        // index 1 MetricLoggerFactory
        // index 2 calling class
        return getLogger(elements[2].getClassName(), logsExternalInteractions);
    }

    static public MetricLogger getLogger(Class _class, boolean logsExternalInteractions) {
        return getLogger(_class.getCanonicalName(), logsExternalInteractions);
    }

    static public MetricLogger getLogger(String name, boolean logsExternalInteractions) {
        MetricLogger got = serviceLoggers.get(name);
        if (got != null) {
            got.debug("ServiceFactory is being called more that once for the same class:" + name);
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                got.debug(i + " " + elements[i].getClassName());
            }
            return got;
        }
        LoggerSummary loggerSummary = (logsExternalInteractions) ? LoggerSummary.INSTANCE_EXTERNAL_INTERACTIONS : LoggerSummary.INSTANCE;
        MetricLogger serviceLogger = new MetricLogger(name, loggerSummary);
        serviceLoggers.putIfAbsent(name, serviceLogger);
        return serviceLogger;
    }

    static public void setLogLevel(Class<?> _class, Level level) {
        setLogLevel(_class.getCanonicalName(), level);
    }

    static public void setLogLevel(String name, Level level) {
        setLogLevel(name, false, level);
    }

    static public void setLogLevel(String name, boolean logsExternalInteractions, Level level) {
        MetricLogger got = getLogger(name, logsExternalInteractions);
        if (got != null) {
            if (got.logger instanceof org.apache.logging.log4j.core.Logger) {
                ((org.apache.logging.log4j.core.Logger) got.logger).setLevel(level);
            } else {
                got.warn("Failed to set level for this logger because it is not an instance of org.apache.logging.log4j.core.Logger");
            }
        }
    }
}
