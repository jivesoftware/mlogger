package com.jivesoftware.os.mlogger.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of BucketedCounterMXBean, which divides past time into buckets (a bucket = n milliseconds)
 * and keeps the number of incr() calls in each bucket. When getValue() is called it will return the sum of
 * counts in all non-expired buckets.
 */
public final class BucketedCounter implements BucketedCounterMXBean {

    private ValueType type;

    private final long bucketSize;
    private final int numberOfBuckets;
    private final Buckets<Long, AtomicLong> bucketedCount;

    public static class Buckets<K, V> extends LinkedHashMap<K, V> {

        private final int capacity;

        public Buckets(int capacity) {
            super();    // insertion-ordered by default
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return this.size() > capacity;
        }
    }

    public BucketedCounter(ValueType type, long bucketSize, int numberOfBuckets) {
        this.type = type;
        this.bucketSize = bucketSize;
        this.numberOfBuckets = numberOfBuckets;
        this.bucketedCount = new Buckets<Long, AtomicLong>(numberOfBuckets + 1);
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\":\"");
        sb.append(type.name());
        sb.append("\",");
        sb.append("\"value\":");
        sb.append(getValue());
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
    public long getValue(int maxNumberOfBuckets) {
        long value = 0L;
        long currentBucketKey = (System.currentTimeMillis()) / bucketSize;
        for (Map.Entry<Long, AtomicLong> bucket : bucketedCount.entrySet()) {
            if (currentBucketKey - bucket.getKey() >= maxNumberOfBuckets) {
                continue;
            }
            value += bucket.getValue().get();
        }

        return value;
    }

    @Override
    public long getValue() {
        return getValue(this.numberOfBuckets);
    }

    @Override
    public String getType() {
        return type.name();
    }

    public void reset() {
        for (Map.Entry<Long, AtomicLong> bucket : bucketedCount.entrySet()) {
            bucket.getValue().set(0L);
        }
    }

    public void inc() {
        incrementBucketValue(1);
    }

    public void inc(long amount) {
        incrementBucketValue(amount);
    }

    public void dec() {
        incrementBucketValue(-1);
    }

    public void dec(long amount) {
        incrementBucketValue(-amount);
    }

    private void incrementBucketValue(long amount) {
        Long bucketKey = (System.currentTimeMillis()) / bucketSize;
        AtomicLong bucketValue = bucketedCount.get(bucketKey);
        if (bucketValue != null) {
            bucketValue.addAndGet(amount);
        } else {
            synchronized (this) {
                bucketValue = bucketedCount.get(bucketKey);
                if (bucketValue != null) {
                    bucketValue.addAndGet(amount);
                } else {
                    bucketedCount.put(bucketKey, new AtomicLong(amount));
                }
            }
        }
    }

    public long getCount(int maxNumberOfBuckets) {
        return getValue(maxNumberOfBuckets);
    }

    public long getCount() {
        return getValue();
    }
}
