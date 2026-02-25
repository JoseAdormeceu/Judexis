package io.judexis.core.check;

import io.judexis.core.data.PlayerData;
import io.judexis.core.domain.PlayerProfile;
import io.judexis.core.snapshot.Snapshot;
import io.judexis.core.violation.EvidenceRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages check lifecycle and snapshot dispatching.
 */
public final class CheckManager {
    private final CheckRegistry registry;
    private final CheckConfiguration configuration;
    private final EvidenceRouter evidenceRouter;
    private volatile CheckDescriptor[] activeChecks;

    public CheckManager(CheckRegistry registry, CheckConfiguration configuration, EvidenceRouter evidenceRouter) {
        this.registry = registry;
        this.configuration = configuration;
        this.evidenceRouter = evidenceRouter;
        this.activeChecks = new CheckDescriptor[0];
    }

    public synchronized void register(String id, CheckCategory category, Check check) {
        CheckDescriptor descriptor = new CheckDescriptor(id, category, check);
        registry.register(descriptor);
        if (configuration.isEnabled(id, true)) {
            descriptor.getCheck().onLoad();
        }
        rebuildActiveChecks();
    }

    public synchronized void setEnabled(String checkId, boolean enabled) {
        CheckDescriptor descriptor = registry.get(checkId);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown check: " + checkId);
        }
        boolean currentlyEnabled = configuration.isEnabled(checkId, true);
        configuration.setEnabled(checkId, enabled);
        if (currentlyEnabled && !enabled) {
            descriptor.getCheck().onUnload();
        } else if (!currentlyEnabled && enabled) {
            descriptor.getCheck().onLoad();
        }
        rebuildActiveChecks();
    }

    public boolean isEnabled(String checkId) {
        return configuration.isEnabled(checkId, true);
    }

    public boolean isRegistered(String checkId) {
        return registry.contains(checkId);
    }

    public synchronized void stop() {
        List<CheckDescriptor> all = registry.all();
        for (int i = 0; i < all.size(); i++) {
            CheckDescriptor descriptor = all.get(i);
            if (configuration.isEnabled(descriptor.getId(), true)) {
                descriptor.getCheck().onUnload();
            }
        }
        activeChecks = new CheckDescriptor[0];
    }

    public void dispatch(PlayerProfile profile, PlayerData playerData, Snapshot snapshot) {
        CheckDescriptor[] local = activeChecks;
        for (int i = 0; i < local.length; i++) {
            CheckDescriptor descriptor = local[i];
            CheckContext context = new CheckContext(profile, playerData, evidenceRouter, descriptor.getId());
            descriptor.getCheck().onSnapshot(context, snapshot);
        }
    }

    private void rebuildActiveChecks() {
        List<CheckDescriptor> all = registry.all();
        List<CheckDescriptor> enabled = new ArrayList<CheckDescriptor>(all.size());
        for (int i = 0; i < all.size(); i++) {
            CheckDescriptor descriptor = all.get(i);
            if (configuration.isEnabled(descriptor.getId(), true)) {
                enabled.add(descriptor);
            }
        }
        activeChecks = enabled.toArray(new CheckDescriptor[enabled.size()]);
    }
}
