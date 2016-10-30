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

import java.util.concurrent.atomic.LongAdder;

public final class AtomicCounter implements AtomicCounterMXBean {

    private ValueType type;
    private final LongAdder value = new LongAdder();

    public AtomicCounter() {
    }

    public AtomicCounter(ValueType type) {
        this.type = type;
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\":\"");
        sb.append(type.name());
        sb.append("\",");
        sb.append("\"value\":");
        sb.append(value.longValue());
        sb.append("}");
        return sb.toString();
    }

    public ValueType getValueType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @Override
    public long getValue() {
        return value.longValue();
    }

    public void setValue(long value) {
        this.value.reset();
        this.value.add(value);
    }

    @Override
    public String getType() {
        return type.name();
    }

    public void reset() {
        this.value.reset();
    }

    public void inc() {
        value.increment();
    }

    public void inc(long amount) {
        value.add(amount);
    }

    public void dec() {
        value.decrement();
    }

    public void dec(long amount) {
        value.add(-amount);
    }

    public void set(long value) {
        this.value.reset();
        this.value.add(value);
    }

    public long getCount() {
        return value.longValue();
    }
}
