package com.jivesoftware.os.mlogger.core;

public interface BucketedCounterMXBean {

    public long getValue(int maxNumberOfBuckets);

    public long getValue();

    public String getType();

}
