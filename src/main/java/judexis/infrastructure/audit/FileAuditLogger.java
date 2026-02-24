package judexis.infrastructure.audit;

import judexis.JudexisPlugin;
import judexis.core.audit.AuditLayer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class FileAuditLogger implements AuditLayer {

    private final Logger logger;
    private final FileHandler handler;

    public FileAuditLogger(JudexisPlugin plugin) {
        try {
            File dir = new File(plugin.getDataFolder(), "audit");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(dir, "judexis-" + date + ".log");
            this.handler = new FileHandler(file.getAbsolutePath(), true);
            this.handler.setFormatter(new SimpleFormatter());
            this.logger = Logger.getLogger("JudexisAudit");
            this.logger.setUseParentHandlers(false);
            this.logger.addHandler(handler);
        } catch (IOException e) {
            throw new IllegalStateException("Could not start audit logger", e);
        }
    }

    @Override
    public void log(String actionType, UUID actor, String target, boolean success) {
        logger.info("[Judexis] ACTION=" + actionType + " ACTOR=" + actor + " TARGET=" + target + " RESULT=" + (success ? "success" : "failure"));
    }

    @Override
    public void close() {
        handler.flush();
        handler.close();
    }
}
