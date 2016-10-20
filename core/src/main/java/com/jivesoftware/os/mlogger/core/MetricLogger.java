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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is an wrapper around a log4j logger that makes it as easy as possible for the developer to gather metrics.
 *
 * @author jonathan
 */
public final class MetricLogger {

    final public CountersAndTimers countersAndTimers;

    final LazyCounter loggerErrorsCount;

    final LazyCounter loggerWarnsCount;

    final LazyCounter loggerDebugsCount;

    final LazyCounter loggerTracesCount;

    final LazyCounter loggerInfosCount;

    final Logger logger;

    final String fullQualifiedClassName;

    final LoggerSummary loggerSummary;

    MetricLogger(String fullQualifiedClassName, LoggerSummary loggerSummary) {
        this.fullQualifiedClassName = fullQualifiedClassName;
        countersAndTimers = CountersAndTimers.getOrCreate(fullQualifiedClassName);
        logger = (Logger) LogManager.getLogger(fullQualifiedClassName);
        loggerErrorsCount = new LazyCounter(countersAndTimers, ValueType.COUNT, "logged.errors");
        loggerWarnsCount = new LazyCounter(countersAndTimers, ValueType.COUNT, "logged.warns");
        loggerDebugsCount = new LazyCounter(countersAndTimers, ValueType.COUNT, "logged.debugs");
        loggerTracesCount = new LazyCounter(countersAndTimers, ValueType.COUNT, "logged.traces");
        loggerInfosCount = new LazyCounter(countersAndTimers, ValueType.COUNT, "logged.infos");
        this.loggerSummary = loggerSummary;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return logger.getName();
    }

    /**
     * Is the underlying log4j logger enabled at Level.TRACE level.
     */
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * Logs a String message at Level.TRACE.
     *
     * @param msg null is ok
     */
    public void trace(String msg) {
        if (!isTraceEnabled()) {
            return;
        }

        loggerSummary.traces++;
        loggerTracesCount.inc();
        logger.log(Level.TRACE, msg);
    }

    /**
     * Logs a message at Level.TRACE after having substituted the 'arg' into the 'messagePattern' at location designated by '{}'.
     *
     * Example:
     *      String fieldName = "foo";
     *      LOG.trace("fieldName:{}", fieldName);
     *
     * Yields:
     *      "fieldName:foo"
     *
     * @param messagePattern null is ok.
     * @param arg null is ok.
     */
    public void trace(String messagePattern, Object arg) {
        if (!isTraceEnabled()) {
            return;
        }

        loggerSummary.traces++;
        loggerTracesCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg);
        logger.log(Level.TRACE, msgStr);
    }

    /**
     * Logs a message at Level.TRACE after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void trace(String messagePattern, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            loggerSummary.traces++;
            loggerTracesCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2);
            logger.log(Level.TRACE, msgStr);
        }
    }

    /**
     * Logs a message at Level.TRACE after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void trace(String messagePattern, Object arg1, Object arg2, Object arg3) {
        if (isTraceEnabled()) {
            loggerSummary.traces++;
            loggerTracesCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2, arg3);
            logger.log(Level.TRACE, msgStr);
        }
    }

    /**
     * Logs a message at Level.TRACE after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void trace(String messagePattern, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (isTraceEnabled()) {
            loggerSummary.traces++;
            loggerTracesCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2, arg3, arg4);
            logger.log(Level.TRACE, msgStr);
        }
    }

    /**
     * Logs a message at Level.TRACE with an exception after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by
     * '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     * @param t null is NOT ok.
     */
    public void trace(String messagePattern, Object[] argArray, Throwable t) {
        if (!isTraceEnabled()) {
            return;
        }

        loggerSummary.traces++;
        loggerSummary.traceThrown(t);
        loggerTracesCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.TRACE, msgStr, t);
    }

    /**
     * Logs a message at Level.TRACE after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void trace(String messagePattern, Object... argArray) {
        if (!isTraceEnabled()) {
            return;
        }

        loggerSummary.traces++;
        loggerTracesCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.TRACE, msgStr);
    }

    /**
     * Logs a String message at Level.TRACE with an exception.
     *
     * @param msg null is ok.
     * @param t null is NOT ok.
     */
    public void trace(String msg, Throwable t) {
        if (!isTraceEnabled()) {
            return;
        }

        loggerSummary.traces++;
        loggerSummary.traceThrown(t);
        loggerTracesCount.inc();
        logger.log(Level.TRACE, msg, t);
    }

    /**
     * Is the underlying log4j logger enabled at Level.DEBUG level.
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * Logs a String message at Level.DEBUG.
     *
     * @param msg null is ok.
     */
    public void debug(String msg) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        loggerSummary.debugs++;
        loggerDebugsCount.inc();
        logger.log(Level.DEBUG, msg);
    }

    /**
     * Logs a message at Level.DEBUG after having substituted the 'arg' into the 'messagePattern' at location designated by '{}'.
     *
     * Example:
     *      String fieldName = "foo";
     *      LOG.trace("fieldName:{}", fieldName);
     *
     * Yields:
     *      "fieldName:foo"
     *
     * @param messagePattern null is ok.
     * @param arg null is ok.
     */
    public void debug(String messagePattern, Object arg) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        loggerSummary.debugs++;
        loggerDebugsCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg);
        logger.log(Level.DEBUG, msgStr);
    }

    /**
     * Logs a message at Level.DEBUG after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void debug(String messagePattern, Object arg1, Object arg2) {
        if (logger.isDebugEnabled()) {
            loggerSummary.debugs++;
            loggerDebugsCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2);
            logger.log(Level.DEBUG, msgStr);
        }
    }

    /**
     * Shorthand for {@link #debug(String, Object[])}
     */
    public void debug(String messagePattern, Object arg1, Object arg2, Object arg3) {
        if (logger.isDebugEnabled()) {
            loggerSummary.debugs++;
            loggerDebugsCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2, arg3);
            logger.log(Level.DEBUG, msgStr);
        }
    }

    /**
     * Shorthand for {@link #debug(String, Object[])}
     */
    public void debug(String messagePattern, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (logger.isDebugEnabled()) {
            loggerSummary.debugs++;
            loggerDebugsCount.inc();
            String msgStr = MessageFormatter.format(messagePattern, arg1, arg2, arg3, arg4);
            logger.log(Level.DEBUG, msgStr);
        }
    }

    /**
     * Logs a message at Level.DEBUG with an exception after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by
     * '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar"; L
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     * @param t null is NOT ok.
     */
    public void debug(String messagePattern, Object[] argArray, Throwable t) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        loggerSummary.debugs++;
        loggerSummary.debugThrown(t);
        loggerDebugsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.DEBUG, msgStr, t);
    }

    /**
     * Logs a message at Level.DEBUG after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void debug(String messagePattern, Object... argArray) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        loggerSummary.debugs++;
        loggerDebugsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.DEBUG, msgStr);
    }

    /**
     * Logs a String message at Level.DEBUG with an exception.
     *
     * @param msg null is ok.
     * @param t null is NOT ok.
     */
    public void debug(String msg, Throwable t) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        loggerSummary.debugs++;
        loggerSummary.debugThrown(t);
        loggerDebugsCount.inc();
        logger.log(Level.DEBUG, msg, t);
    }

    /**
     * Is the underlying log4j logger enabled at Level.WARN level.
     */
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * Logs a String message at Level.WARN.
     *
     * @param msg null is ok
     */
    public void warn(String msg) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerWarnsCount.inc();
        logger.log(Level.WARN, msg);
        loggerSummary.lastNWarns.add(msg);
    }

    /**
     * Logs a message at Level.WARN after having substituted the 'arg' into the 'messagePattern' at location designated by '{}'.
     *
     * Example:
     *      String fieldName = "foo";
     *      LOG.trace("fieldName:{}", fieldName);
     *
     * Yields:
     *      "fieldName:foo"
     *
     * @param messagePattern null is ok.
     * @param arg null is ok.
     */
    public void warn(String messagePattern, Object arg) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerWarnsCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg);
        logger.log(Level.WARN, msgStr);
        loggerSummary.lastNWarns.add(String.format(messagePattern, arg));
    }

    /**
     * Logs a message at Level.WARN after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void warn(String messagePattern, Object arg1, Object arg2) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerWarnsCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg1, arg2);
        logger.log(Level.WARN, msgStr);
        loggerSummary.lastNWarns.add(String.format(messagePattern, arg1, arg2));
    }

    /**
     * Logs a message at Level.WARN with an exception after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by
     * '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     * @param t null is NOT ok.
     */
    public void warn(String messagePattern, Object[] argArray, Throwable t) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerSummary.warnThrown(t);
        loggerWarnsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.WARN, msgStr, t);
        loggerSummary.lastNWarns.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a message at Level.WARN after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void warn(String messagePattern, Object... argArray) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerWarnsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.WARN, msgStr);
        loggerSummary.lastNWarns.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a String message at Level.WARN with an exception.
     *
     * @param msg null is ok.
     * @param t null is NOT ok.
     */
    public void warn(String msg, Throwable t) {
        if (!logger.isWarnEnabled()) {
            return;
        }

        loggerSummary.warns++;
        loggerSummary.warnThrown(t);
        loggerWarnsCount.inc();
        logger.log(Level.WARN, msg, t);
        loggerSummary.lastNWarns.add(msg + (t != null ? " " + t.toString() : ""));
    }

    /**
     * Is the underlying logback logger enabled at Level.INFO level.
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * Logs a String message at Level.INFO.
     *
     * @param msg null is ok
     */
    public void info(String msg) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerInfosCount.inc();
        logger.log(Level.INFO, msg);
        loggerSummary.lastNInfos.add(msg);
    }

    /**
     * Logs a message at Level.INFO after having substituted the 'arg' into the 'messagePattern' at location designated by '{}'.
     *
     * Example:
     *      String fieldName = "foo";
     *      LOG.trace("fieldName:{}", fieldName);
     *
     * Yields:
     *      "fieldName:foo"
     *
     * @param messagePattern null is ok.
     * @param arg null is ok.
     */
    public void info(String messagePattern, Object arg) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerInfosCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg);
        logger.log(Level.INFO, msgStr);
        loggerSummary.lastNInfos.add(String.format(messagePattern, arg));
    }

    /**
     * Logs a message at Level.INFO after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void info(String messagePattern, Object arg1, Object arg2) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerInfosCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg1, arg2);
        logger.log(Level.INFO, msgStr);
        loggerSummary.lastNInfos.add(String.format(messagePattern, arg1, arg2));
    }

    /**
     * Logs a message at Level.INFO with an exception after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by
     * '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void info(String messagePattern, Object[] argArray, Throwable t) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerSummary.infoThrown(t);
        loggerInfosCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.INFO, msgStr, t);
        loggerSummary.lastNInfos.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a message at Level.INFO after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void info(String messagePattern, Object... argArray) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerInfosCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.INFO, msgStr);
        loggerSummary.lastNInfos.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a String message at Level.INFO with an exception.
     *
     * @param messagePattern null is ok.
     * @param t null is NOT ok.
     */
    public void info(String messagePattern, Throwable t) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        loggerSummary.infos++;
        loggerSummary.infoThrown(t);
        loggerInfosCount.inc();
        logger.log(Level.INFO, messagePattern, t);
        loggerSummary.lastNInfos.add(messagePattern + (t != null ? " " + t.toString() : ""));
    }

    /**
     * Is the underlying log4j logger enabled at Level.ERROR level.
     */
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    /**
     * Logs a String message at Level.ERROR.
     *
     * @param msg null is ok
     */
    public void error(String msg) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        logger.log(Level.ERROR, msg);
        loggerSummary.lastNErrors.add(msg);
    }

    /**
     * Logs a String message at Level.ERROR.
     *
     * @param tags null is ok
     * @param msg null is ok
     */
    public void error(String[] tags, String msg) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        logger.log(Level.ERROR, msg);
        loggerSummary.lastNErrors.add(msg);
    }

    /**
     * Logs a message at Level.ERROR after having substituted the 'arg' into the 'messagePattern' at location designated by '{}'.
     *
     * Example:
     *      String fieldName = "foo";
     *      LOG.trace("fieldName:{}", fieldName);
     *
     * Yields:
     *      "fieldName:foo"
     *
     * @param messagePattern null is ok.
     * @param arg null is ok.
     */
    public void error(String messagePattern, Object arg) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg);
        logger.log(Level.ERROR, msgStr);
        loggerSummary.lastNErrors.add(String.format(messagePattern, arg));
    }

    /**
     * Logs a message at Level.ERROR after having substituted the 'arg1' and 'arg2' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", fieldName, value);
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param arg1 null is ok.
     * @param arg2 null is ok.
     */
    public void error(String messagePattern, Object arg1, Object arg2) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        String msgStr = MessageFormatter.format(messagePattern, arg1, arg2);
        logger.log(Level.ERROR, msgStr);
        loggerSummary.lastNErrors.add(String.format(messagePattern, arg1, arg2));
    }

    /**
     * Logs a message at Level.ERROR with an exception after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by
     * '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     * @param t null is NOT ok.
     */
    public void error(String messagePattern, Object[] argArray, Throwable t) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerSummary.errorThrown(t);
        loggerErrorsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.ERROR, msgStr, t);
        loggerSummary.lastNErrors.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a message at Level.ERROR after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void error(String messagePattern, Object... argArray) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.ERROR, msgStr);
        loggerSummary.lastNErrors.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a message at Level.ERROR after having substituted the elements of 'argArray' into the 'messagePattern' at locations designated by '{}'s.
     *
     * Example:
     *      String fieldName = "foo";
     *      String value = "bar";
     *      LOG.trace("fieldName:{}={}", new Object[]{fieldName,value});
     *
     * Yields:
     *      "fieldName:foo=bar"
     *
     * @param tags null is ok.
     * @param messagePattern null is ok.
     * @param argArray null is ok.
     */
    public void error(String[] tags, String messagePattern, Object[] argArray) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerErrorsCount.inc();
        String msgStr = MessageFormatter.arrayFormat(messagePattern, argArray);
        logger.log(Level.ERROR, msgStr);
        loggerSummary.lastNErrors.add(String.format(messagePattern, argArray));
    }

    /**
     * Logs a String message at Level.ERROR with an exception.
     *
     * @param messagePattern null is ok.
     * @param t null is NOT ok.
     */
    public void error(String messagePattern, Throwable t) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerSummary.errorThrown(t);
        loggerErrorsCount.inc();
        logger.log(Level.ERROR, messagePattern, t);
        loggerSummary.lastNErrors.add(messagePattern + (t != null ? " " + t.toString() : ""));
    }

    /**
     * Logs a String[] of tags with String message at Level.ERROR with an exception.
     *
     * @param messagePattern null is ok.
     * @param t null is NOT ok.
     */
    public void error(String[] tags, String messagePattern, Throwable t) {
        if (!logger.isErrorEnabled()) {
            return;
        }

        loggerSummary.errors++;
        loggerSummary.errorThrown(t);
        loggerErrorsCount.inc();
        logger.log(Level.ERROR, messagePattern, t);
        loggerSummary.lastNErrors.add(messagePattern + (t != null ? " " + t.toString() : ""));
    }

    /**
     * Allows you set the ValueType the name and the value for a specific metric.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *      LOG.set(ValueType.COUNT, "foo>bar>a", 1);
     *      LOG.set(ValueType.COUNT, "foo>bar>b", 2);
     *      LOG.set(ValueType.COUNT, "foo>bazz>c", 3);
     *
     * Yields:
     *      foo
     *      |-- bar
     *      |   |-- a = 1
     *      |   |-- b = 2
     *      |-- baz
     *      |   |-- c = 3
     *
     * @param type null NOT ok.
     * @param name null NOT ok.
     * @param value null NOT ok.
     */
    public void set(ValueType type, String name, long value) {
        countersAndTimers.counter(type, name).set(value);
    }

    public void set(ValueType type, String name, long value, String tenant) {
        countersAndTimers.getTenantMetric(tenant).counter(type, name).set(value);
    }

    /**
     * Same as set(ValueType type, String name, long value) except it uses an AtomicLong. This is notably slower than the simple set(ValueType type, String
     * name, long value) form. Only use if you absolutely have to have an accurate count.
     *
     * @param type null NOT ok.
     * @param name null NOT ok.
     * @param value null NOT ok.
     */
    public void setAtomic(ValueType type, String name, long value) {
        countersAndTimers.atomicCounter(type, name).set(value);
    }

    public void setAtomic(ValueType type, String name, long value, String tenant) {
        countersAndTimers.getTenantMetric(tenant).atomicCounter(type, name).set(value);
    }

    /**
     * Increments a named long. Counts are not guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     *For example:
     *      LOG.inc("foo>bar>a");
     *      LOG.inc("foo>bar>b");
     *      LOG.inc("foo>bazz>c");
     *
     * Yields:
     *      foo
     *      |-- bar
     *      |   |-- a = 1
     *      |   |-- b = 1
     *      |-- baz
     *      |   |-- c = 1
     *
     * @param name null NOT ok.
     */
    public void inc(String name) {
        countersAndTimers.counter(ValueType.COUNT, name).inc();
    }

    public void inc(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).counter(ValueType.COUNT, name).inc();
    }

    /**
     * Increments a named long by an amount. Counts are not guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *      LOG.inc("foo>bar>a",10);
     *      LOG.inc("foo>bar>b",20);
     *      LOG.inc("foo>bazz>c",20);
     *
     * Yields:
     *      foo
     *      |-- bar
     *      |   |-- a = 10
     *      |   |-- b = 20
     *      |-- baz
     *      |   |-- c = 30
     *
     * @param name null NOT ok.
     * @param amount negative numbers are ok.
     */
    public void inc(String name, long amount) {
        countersAndTimers.counter(ValueType.COUNT, name).inc(amount);
    }

    public void inc(String name, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).counter(ValueType.COUNT, name).inc(amount);
    }

    /**
     * Opposite of {@link #inc(String name)}
     *
     * @param name null NOT ok.
     */
    public void dec(String name) {
        countersAndTimers.counter(ValueType.COUNT, name).dec();
    }

    public void dec(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).counter(ValueType.COUNT, name).dec();
    }

    /**
     * Opposite of {@link #inc(String name, long amount)}
     *
     * @param name of the counter
     * @param amount for the counter
     */
    public void dec(String name, long amount) {
        countersAndTimers.counter(ValueType.COUNT, name).dec(amount);
    }

    public void dec(String name, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).counter(ValueType.COUNT, name).dec(amount);
    }

    /**
     * Increments a named AtomicLong. Counts guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *      LOG.incAtomic("foo>bar>a");
     *      LOG.incAtomic("foo>bar>b");
     *      LOG.incAtomic("foo>bazz>c");
     *
     * Yields:
     *      foo
     *      |-- bar
     *      |   |-- a = 1
     *      |   |-- b = 1
     *      |-- baz
     *      |   |-- c = 1
     *
     * @param name null NOT ok.
     */
    public void incAtomic(String name) {
        countersAndTimers.atomicCounter(ValueType.COUNT, name).inc();
    }

    public void incAtomic(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).atomicCounter(ValueType.COUNT, name).inc();
    }

    /**
     * Increments a named AtomicLong by an amount. Counts are guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *      LOG.incAtomic("foo>bar>a",10);
     *      LOG.incAtomic("foo>bar>b",20);
     *      LOG.incAtomic("foo>bazz>c",20);
     *
     * Yields:
     *      foo
     *      |-- bar
     *      |   |-- a = 10
     *      |   |-- b = 20
     *      |-- baz
     *      |   |-- c = 30
     *
     * @param name null NOT ok.
     * @param amount negative numbers are ok.
     */
    public void incAtomic(String name, long amount) {
        countersAndTimers.atomicCounter(ValueType.COUNT, name).inc(amount);
    }

    public void incAtomic(String name, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).atomicCounter(ValueType.COUNT, name).inc(amount);
    }

    /**
     * Opposite of {@link #incAtomic(String name)}
     *
     * @param name null NOT ok.
     */
    public void decAtomic(String name) {
        countersAndTimers.atomicCounter(ValueType.COUNT, name).dec();
    }

    public void decAtomic(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).atomicCounter(ValueType.COUNT, name).dec();
    }

    /**
     * Opposite of {@link #incAtomic(String name, long amount)}
     *
     * @param name null NOT ok.
     * @param amount null NOT ok.
     */
    public void decAtomic(String name, long amount) {
        countersAndTimers.atomicCounter(ValueType.COUNT, name).dec(amount);
    }

    public void decAtomic(String name, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).atomicCounter(ValueType.COUNT, name).dec(amount);
    }
    /**
     * Split the past time into buckets and increments a named AtomicLong in current bucket by one.
     * Counts are guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    LOG.incBucket("foo>bar>a",10);
     *    LOG.incBucket("foo>bar>b",20);
     *    LOG.incBucket("foo>bazz>c",20);
     *
     * @param name null NOT ok.
     * @param bucketSize   the length of bucket, in milliseconds
     * @param numberOfBuckets    the number of buckets to be looked back, when computing total count in all buckets
     */
    public void incBucket(String name, long bucketSize, int numberOfBuckets) {
        countersAndTimers.bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).inc();
    }

    public void incBucket(String name, long bucketSize, int numberOfBuckets, String tenant) {
        countersAndTimers.getTenantMetric(tenant).bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).inc();
    }

    /**
     * Split the past time into buckets and increments a named AtomicLong in current bucket by specified amount.
     * Counts are guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    LOG.incBucket("foo>bar>a",10);
     *    LOG.incBucket("foo>bar>b",20);
     *    LOG.incBucket("foo>bazz>c",20);
     *
     * @param name null NOT ok.
     * @param bucketSize   the length of bucket, in milliseconds
     * @param numberOfBuckets    the number of buckets to be looked back, when computing total count in all buckets
     * @param amount   the amount to be added on the AtomicLong.
     */
    public void incBucket(String name, long bucketSize, int numberOfBuckets, long amount) {
        countersAndTimers.bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).inc(amount);
    }

    public void incBucket(String name, long bucketSize, int numberOfBuckets, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).inc(amount);
    }

    /**
     * Split the past time into buckets and decrements a named AtomicLong in current bucket by one.
     * Counts are guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    LOG.incBucket("foo>bar>a",10);
     *    LOG.incBucket("foo>bar>b",20);
     *    LOG.incBucket("foo>bazz>c",20);
     *
     * @param name null NOT ok.
     * @param bucketSize   the length of bucket, in milliseconds
     * @param numberOfBuckets    the number of buckets to be looked back, when computing total count in all buckets
     */
    public void decBucket(String name, long bucketSize, int numberOfBuckets) {
        countersAndTimers.bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).dec();
    }

    public void decBucket(String name, long bucketSize, int numberOfBuckets, String tenant) {
        countersAndTimers.getTenantMetric(tenant).bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).dec();
    }

    /**
     * Split the past time into buckets and decrements a named AtomicLong in current bucket by specified amount.
     * Counts are guaranteed to be exact.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    LOG.incBucket("foo>bar>a",10);
     *    LOG.incBucket("foo>bar>b",20);
     *    LOG.incBucket("foo>bazz>c",20);
     *
     * @param name null NOT ok.
     * @param bucketSize   the length of bucket, in milliseconds
     * @param numberOfBuckets    the number of buckets to be looked back, when computing total count in all buckets
     * @param amount   the amount to be deducted from the AtomicLong.
     */
    public void decBucket(String name, long bucketSize, int numberOfBuckets, long amount) {
        countersAndTimers.bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).dec(amount);
    }

    public void decBucket(String name, long bucketSize, int numberOfBuckets, long amount, String tenant) {
        countersAndTimers.getTenantMetric(tenant).bucketedCounter(ValueType.COUNT, name, bucketSize, numberOfBuckets).dec(amount);
    }

    /**
     * Starts a named timer. Each time a time is started and stopped its elapse is
     * added as a sample to org.apache.commons.math.stat.descriptive.SummaryStatistics;
     *
     * Each threads timings are captured discreetly. If the same thread re-enters start before stop is called
     * it will not advance the start time.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    try {
     *      LOG.startTimer("foo>bar>a");
     *      LOG.startTimer("foo>bar>b");
     *      LOG.startTimer("foo>bazz>c");
     *
     *      .......
     *    } finally {
     *       LOG.stopTimer("foo>bar>a");
     *       LOG.stopTimer("foo>bar>b");
     *       LOG.stopTimer("foo>bazz>c");
     *    }
     *
     * Yields:
     *    foo
     *    |-- bar
     *    |   |-- a = 234325 (millis)
     *    |   |-- b = 234328 (millis)
     *    |-- baz
     *    |   |-- c = 234333 (millis)
     *
     * @param name null NOT ok.
     */
    public void startTimer(String name) {
        countersAndTimers.startTimer(name);
    }

    public void startTenantTimer(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).startTimer(name);
    }

    /**
     * Stops a named timer. See: {@link #startTimer(String name)} This is a convenience method that delegates to
     * {@link #stopTimer(java.lang.String, java.lang.String)}
     *
     * @param name null NOT ok.
     * @return elapse in millis
     */
    public long stopTimer(String name) {
        return countersAndTimers.stopTimer(name, name);
    }

    public void stopTenantTimer(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).stopTimer(name, name);
    }

    /**
     * Starts a named timer. Each time a time is started and stopped its elapse is
     * added as a sample to org.apache.commons.math.stat.descriptive.SummaryStatistics;
     *
     * Each threads timings are captured discreetly. If the same thread re-enters start before stop is called
     * it will not advance the start time.
     *
     * Metric names can be organized hierarchically by using the greater than separator.
     * For example:
     *    try {
     *      LOG.startTimer("foo>bar>a");
     *      LOG.startTimer("foo>bar>b");
     *      LOG.startTimer("foo>bazz>c");
     *
     *      .......
     *    } finally {
     *       LOG.stopTimer("foo>bar>a");
     *       LOG.stopTimer("foo>bar>b");
     *       LOG.stopTimer("foo>bazz>c");
     *    }
     *
     * Yields:
     *    foo
     *    |-- bar
     *    |   |-- a = 234325 (millis)
     *    |   |-- b = 234328 (millis)
     *    |-- baz
     *    |   |-- c = 234333 (millis)
     *
     * @param name null NOT ok.
     */
    public void startNanoTimer(String name) {
        countersAndTimers.startNanoTimer(name);
    }

    public void startTenantNanoTimer(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).startNanoTimer(name);
    }

    /**
     * Stops a named timer. See: {@link #startTimer(String name)} This is a convenience method that delegates to
     * {@link #stopTimer(java.lang.String, java.lang.String)}
     *
     * @param name null NOT ok.
     * @return elapse in millis
     */
    public long stopNanoTimer(String name) {
        return countersAndTimers.stopNanoTimer(name, name);
    }

    public void stopTenantNanoTimer(String name, String tenant) {
        countersAndTimers.getTenantMetric(tenant).stopNanoTimer(name, name);
    }

    /**
     * Stops a named timer. See: {@link #startTimer(String name)}
     *
     * When you have a method with multiply returns this method allows you to return the timings around each distinct return.
     *
     * Example: try { logger.startTimer("foo"); if (bar) { // do if stuff stopTimer("foo","firstReturn"); return; } else { // do else stuff
     * stopTimer("foo","secondReturn"); return; } } finally { stopTimer("foo","all"); }
     *
     * @param name null NOT ok.
     * @param recordedName null NOT ok.
     * @return elapse in millis
     */
    public long stopTimer(String name, String recordedName) {
        return countersAndTimers.stopTimer(name, recordedName);
    }

    public long stopTenantTimer(String name, String recordedName, String tenant) {
        return countersAndTimers.getTenantMetric(tenant).stopTimer(name, recordedName);
    }

    /**
     * Start named timed operation and return {@link TimedOperation} for additional functionallity.<br/>
     * The name parameter is combined with the logger name so there is no need to excplicitly provide it.<br/>
     * {@link TimedOperation} is {@link AutoCloseable} to it can be used inside try block.<br/>
     *
     * @param name the name of the named timed operation
     * @return new operation object
     */
    public TimedOperation startTimedOperation(String name) {
        return new TimedOperation(this, name);
    }

    /**
     * Start named timed operation and return {@link TimedOperation} for additional functionallity.<br/>
     * The name parameter is combined with the logger name so there is no need to excplicitly provide it.<br/>
     * {@link TimedOperation} is {@link AutoCloseable} to it can be used inside try block.<br/>
     *
     * @param name the name of the named timed operation
     * @param initialStatus the initial status to set on the operation.
     * @return new operation object
     */
    public TimedOperation startTimedOperation(String name, TimedOperation.Status initialStatus) {
        return new TimedOperation(this, name, initialStatus);
    }

    /**
     * Start named timed operation and return {@link TimedOperation} for additional functionallity.<br/>
     * The name parameter is combined with the logger name so there is no need to excplicitly provide it.<br/>
     * {@link TimedOperation} is {@link AutoCloseable} to it can be used inside try block.<br/>
     *
     * @param name the name of the named timed operation
     * @param tenantId The tenant that is executing the operation.
     * @return new operation object
     */
    public TimedOperation startTimedOperation(String name, Object tenantId) {
        return new TimedOperation(this, name, tenantId);
    }

    /**
     * Start named timed operation and return {@link TimedOperation} for additional functionallity.<br/>
     * The name parameter is combined with the logger name so there is no need to excplicitly provide it.<br/>
     * {@link TimedOperation} is {@link AutoCloseable} to it can be used inside try block.<br/>
     *
     * @param name the name of the named timed operation
     * @param tenantId The tenant that is executing the operation.
     * @param initialStatus the initial status to set on the operation.
     * @return new operation object
     */
    public TimedOperation startTimedOperation(String name, Object tenantId, TimedOperation.Status initialStatus) {
        return new TimedOperation(this, name, tenantId, initialStatus);
    }

    /**
     * Log a Metrics Event
     *
     * @param name Name of the metric. Try to keep this unique: [jira project]_[feature name]_[event name]
     * @return Metric object which you'll put metric properties onto, and then send().
     */
    public Metric metric(String name) {
        return MetricEvent.metric(name);
    }

}
