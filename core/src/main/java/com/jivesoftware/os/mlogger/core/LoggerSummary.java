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

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 * @author jonathan
 */
public class LoggerSummary {

    private static final ISO8601DateFormat DATE_FORMAT = new ISO8601DateFormat(TimeZone.getTimeZone("GMT"));
    public static LoggerSummary INSTANCE = new LoggerSummary();
    public static LoggerSummary INSTANCE_EXTERNAL_INTERACTIONS = new LoggerSummary();

    public long infos;
    public long debugs;
    public long traces;
    public long warns;
    public long errors;

    public LastN<String> lastNInfos = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };
    public LastN<String> lastNWarns = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };
    public LastN<String> lastNErrors = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };

    public void reset() {
        lastNInfos = new LastN<>(new String[10]);
        infos = 0;
        debugs = 0;
        traces = 0;
        warns = 0;
        errors = 0;
    }

    private Map<String, Thrown> infoThrown = new ConcurrentHashMap<>(16, 0.75f, 64);
    private Map<String, Thrown> debugsThrown = new ConcurrentHashMap<>(16, 0.75f, 64);
    private Map<String, Thrown> tracesThrown = new ConcurrentHashMap<>(16, 0.75f, 64);
    private Map<String, Thrown> warnsThrown = new ConcurrentHashMap<>(16, 0.75f, 64);
    private Map<String, Thrown> errorsThrown = new ConcurrentHashMap<>(16, 0.75f, 64);

    public void infoThrown(Throwable throwable) {
        compute(infoThrown, throwable);
    }

    public Collection<Thrown> infoThrowables() {
        return infoThrown.values();
    }

    public void debugThrown(Throwable throwable) {
        compute(debugsThrown, throwable);
    }

    public Collection<Thrown> debugThrowables() {
        return debugsThrown.values();
    }

    public void traceThrown(Throwable throwable) {
        compute(tracesThrown, throwable);
    }

    public Collection<Thrown> traceThrowables() {
        return tracesThrown.values();
    }

    public void warnThrown(Throwable throwable) {
        compute(warnsThrown, throwable);
    }

    public Collection<Thrown> warnThrowables() {
        return warnsThrown.values();
    }

    public void errorThrown(Throwable throwable) {
        compute(errorsThrown, throwable);
    }

    public Collection<Thrown> errorThrowables() {
        return errorsThrown.values();
    }

    private void compute(Map<String, Thrown> thrown, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        Thrown t = thrown(throwable);
        thrown.compute(t.key, (k, current) -> {
            Thrown compute = current;
            if (current == null) {
                compute = t;
            }
            compute.increment();
            if (throwable.getCause() != null) {
                compute(compute.cause, throwable.getCause());
            }
            return compute;
        });
    }

    public Thrown thrown(Throwable throwable) {
        String message = throwable.getMessage();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement keyStackTrace = stackTrace[0];
        String key = keyStackTrace.getFileName() + "|" + keyStackTrace.getClassName() + "|" + keyStackTrace.getMethodName() + "|" + keyStackTrace
            .getLineNumber() + "|" + keyStackTrace.isNativeMethod();
        return new Thrown(key, message, stackTrace);
    }

    public static class Thrown {

        final public String key;
        final public String message;
        final public StackTraceElement[] stackTrace;
        final public LongAdder thrown;
        public long timestamp;
        final public Map<String, Thrown> cause = new ConcurrentHashMap<>();

        public Thrown(String key, String message, StackTraceElement[] stackTrace) {
            this.key = key;
            this.message = message;
            this.stackTrace = stackTrace;
            this.thrown = new LongAdder();
        }

        public void increment() {
            thrown.increment();
            timestamp = System.currentTimeMillis();
        }
    }
}
