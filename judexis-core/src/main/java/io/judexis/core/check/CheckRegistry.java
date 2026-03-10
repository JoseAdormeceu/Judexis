package io.judexis.core.check;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of all known checks by id.
 */
public final class CheckRegistry {
    private final Map<String, CheckDescriptor> checksById = new LinkedHashMap<String, CheckDescriptor>();

    public synchronized void register(CheckDescriptor descriptor) {
        if (checksById.containsKey(descriptor.getId())) {
            throw new IllegalArgumentException("duplicate check id: " + descriptor.getId());
        }
        checksById.put(descriptor.getId(), descriptor);
    }

    public synchronized CheckDescriptor get(String checkId) {
        return checksById.get(checkId);
    }

    public synchronized boolean contains(String checkId) {
        return checksById.containsKey(checkId);
    }

    public synchronized List<CheckDescriptor> all() {
        return new ArrayList<CheckDescriptor>(checksById.values());
    }
}
