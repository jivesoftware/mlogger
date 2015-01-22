package com.jivesoftware.os.mlogger.health.api;

import com.jivesoftware.os.mlogger.health.HealthCheck;

/**
 *
 * @author jonathan.colt
 * @param <C>
 */
public interface HealthChecker<C> extends HealthCheck {

    public void check(C checkable, String description, String resolution);

}
