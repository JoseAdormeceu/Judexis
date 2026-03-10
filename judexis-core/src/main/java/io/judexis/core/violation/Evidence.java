package io.judexis.core.violation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable evidence unit emitted by checks.
 */
public final class Evidence {
    private final String checkName;
    private final EvidenceType type;
    private final EvidenceSeverity severity;
    private final String detail;
    private final long capturedAtNanos;
    private final Map<String, String> metadata;

    public Evidence(String checkName, EvidenceType type, EvidenceSeverity severity,
                    String detail, long capturedAtNanos, Map<String, String> metadata) {
        if (checkName == null || checkName.trim().isEmpty()) {
            throw new IllegalArgumentException("checkName is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("type is required");
        }
        if (severity == null) {
            throw new IllegalArgumentException("severity is required");
        }
        this.checkName = checkName;
        this.type = type;
        this.severity = severity;
        this.detail = detail == null ? "" : detail;
        this.capturedAtNanos = capturedAtNanos;
        this.metadata = metadata == null
            ? Collections.<String, String>emptyMap()
            : Collections.unmodifiableMap(new HashMap<String, String>(metadata));
    }

    public String getCheckName() {
        return checkName;
    }

    public EvidenceType getType() {
        return type;
    }

    public EvidenceSeverity getSeverity() {
        return severity;
    }

    public String getDetail() {
        return detail;
    }

    public long getCapturedAtNanos() {
        return capturedAtNanos;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
