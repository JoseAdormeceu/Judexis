package io.judexis.core.check;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory toggle configuration for checks.
 */
public final class CheckConfiguration {
    private final Map<String, Boolean> states = new ConcurrentHashMap<String, Boolean>();

    public boolean isEnabled(String checkId, boolean defaultState) {
        Boolean state = states.get(checkId);
        return state == null ? defaultState : state.booleanValue();
    }

    public void setEnabled(String checkId, boolean enabled) {
        states.put(checkId, Boolean.valueOf(enabled));
    }
}
