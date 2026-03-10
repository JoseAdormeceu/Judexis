package io.judexis.core.decision;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory event stream for decisions.
 */
public final class DecisionEventBus {
    private final List<DecisionEventListener> listeners = new CopyOnWriteArrayList<DecisionEventListener>();

    public void subscribe(DecisionEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(DecisionEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(DecisionEvent event) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDecision(event);
        }
    }
}
