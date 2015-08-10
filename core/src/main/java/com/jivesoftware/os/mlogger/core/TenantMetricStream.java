package com.jivesoftware.os.mlogger.core;

/**
 *
 * @author jonathan.colt
 */
public interface TenantMetricStream {

    boolean stream(String tenant, CountersAndTimers cat);
}
