package judexis.core.audit;

import java.util.UUID;

public interface AuditLayer {
    void log(String actionType, UUID actor, String target, boolean success);
    void close();
}
