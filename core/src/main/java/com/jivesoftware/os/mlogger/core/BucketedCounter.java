package com.jivesoftware.os.mlogger.core;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * Implementation of BucketedCounterMXBean, which divides past time into buckets (a bucket = n milliseconds)
 * and keeps the number of incr() calls in each bucket. When getValue() is called it will return the sum of
 * counts in all non-expired buckets.
 */
public final class BucketedCounter implements BucketedCounterMXBean {

    private ValueType type;

    private final long bucketSize;
    private final int numberOfBuckets;
    private final ConcurrentLinkedHashMap<Long, LongAdder> bucketedCount;

    public BucketedCounter(ValueType type, long bucketSize, int numberOfBuckets) {
        this.type = type;
        this.bucketSize = bucketSize;
        this.numberOfBuckets = numberOfBuckets;
        this.bucketedCount = new ConcurrentLinkedHashMap.Builder<Long, LongAdder>()
            .maximumWeightedCapacity(numberOfBuckets + 1)
            .build();
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
        for (Map.Entry<Long, LongAdder> bucket : bucketedCount.entrySet()) {
            if (currentBucketKey - bucket.getKey() >= maxNumberOfBuckets) {
                continue;
            }
            value += bucket.getValue().longValue();
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
        for (Map.Entry<Long, LongAdder> bucket : bucketedCount.entrySet()) {
            bucket.getValue().reset();
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
        LongAdder bucketValue = bucketedCount.computeIfAbsent(bucketKey, (t) -> new LongAdder());
        bucketValue.add(amount);
    }

    public long getCount(int maxNumberOfBuckets) {
        return getValue(maxNumberOfBuckets);
    }

    public long getCount() {
        return getValue();
    }
}
