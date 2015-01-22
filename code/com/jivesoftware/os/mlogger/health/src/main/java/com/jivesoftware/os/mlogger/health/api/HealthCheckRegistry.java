package com.jivesoftware.os.mlogger.health.api;

/**
 *
 * @author jonathan.colt
 */
public interface HealthCheckRegistry {

    void register(HealthChecker<?> healthChecker);

    void unregister(HealthChecker<?> healthChecker);
}
