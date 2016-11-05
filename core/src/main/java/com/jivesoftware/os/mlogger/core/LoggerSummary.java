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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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

    public final LongAdder infos = new LongAdder();
    public final LongAdder debugs = new LongAdder();
    public final LongAdder traces = new LongAdder();
    public final LongAdder warns = new LongAdder();
    public final LongAdder errors = new LongAdder();

    public final LastN<String> lastNInfos = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };
    public final LastN<String> lastNWarns = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };
    public final LastN<String> lastNErrors = new LastN<String>(new String[10]) {
        @Override
        public void add(String t) {
            super.add(DATE_FORMAT.format(new Date()) + " " + t);
        }
    };

    public void reset() {
        lastNInfos.clear(null);
        lastNWarns.clear(null);
        lastNErrors.clear(null);

        infos.reset();
        debugs.reset();
        traces.reset();
        warns.reset();
        errors.reset();

        thrown.clear();

    }

    private Map<String, Thrown> thrown = new ConcurrentHashMap<>(16, 0.75f, 64);

    public Collection<Thrown> throwables() {
        return thrown.values();
    }

    public void infoThrown(Throwable throwable) {
        compute(thrown, "info", throwable);
    }

    public void debugThrown(Throwable throwable) {
        compute(thrown, "debug", throwable);
    }

    public void traceThrown(Throwable throwable) {
        compute(thrown, "trace", throwable);
    }

    public void warnThrown(Throwable throwable) {
        compute(thrown, "warn", throwable);
    }

    public void errorThrown(Throwable throwable) {
        compute(thrown, "error", throwable);
    }

    private void compute(Map<String, Thrown> thrown, String level, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        Thrown t = thrown(level, throwable);
        thrown.compute(t.key, (k, current) -> {
            Thrown compute = current;
            if (current == null) {
                compute = t;
            }
            compute.increment();
            if (throwable.getCause() != null) {
                compute(compute.cause, level, throwable.getCause());
            }
            return compute;
        });
    }

    public Thrown thrown(String level, Throwable throwable) {
        String package_ =throwable.getClass().getPackage().getName();
        String class_ =throwable.getClass().getSimpleName();

        String message = throwable.getMessage();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement keyStackTrace = stackTrace[0];
        String key = key(level, throwable, stackTrace);
        return new Thrown(key, level, package_, class_, message, stackTrace);
    }

    private String key(String level, Throwable throwable, StackTraceElement[] stackTrace) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return "MD5-failed-instance-hashcode=" + String.valueOf(throwable.hashCode());
        }
        md.update(level.getBytes());
        for (StackTraceElement stackFrame : stackTrace) {
            md.update(stackFrame.getClassName().getBytes());
            md.update(":".getBytes());
            md.update(stackFrame.getMethodName().getBytes());
            md.update(":".getBytes());
            md.update(String.valueOf(stackFrame.getLineNumber()).getBytes());
        }
        return Base64.getEncoder().encodeToString(md.digest());

    }

    public static class Thrown {

        public String key;
        public String level;
        public String package_;
        public String class_;
        public String message;
        public StackTraceElement[] stackTrace;
        public LongAdder thrown;
        public long timestamp = 0;
        public Map<String, Thrown> cause = new ConcurrentHashMap<>();

        public Thrown() {
            this.key = null;
            this.level = null;
            this.package_ = null;
            this.class_ = null;
            this.message = null;
            this.stackTrace = null;
            this.thrown = null;
        }

        public Thrown(String key, String level, String package_, String class_, String message, StackTraceElement[] stackTrace) {
            this.key = key;
            this.level = level;
            this.package_ = package_;
            this.class_ = class_;
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
