package io.judexis.core.check.bootstrap;

import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.check.CheckCategory;
import io.judexis.core.check.movement.VerticalIntegrityCheck;
import io.judexis.core.check.movement.VerticalIntegrityConfig;

/**
 * Registers built-in production checks.
 */
public final class CoreCheckBootstrap {
    private CoreCheckBootstrap() {
    }

    public static void registerDefaults(JudexisCoreEngine engine) {
        registerDefaults(engine, new VerticalIntegrityConfig());
    }

    public static void registerDefaults(JudexisCoreEngine engine, VerticalIntegrityConfig config) {
        engine.registerCheck(VerticalIntegrityCheck.ID, CheckCategory.MOVEMENT, new VerticalIntegrityCheck(config));
    }
}
