package io.judexis.core.check.bootstrap;

import io.judexis.core.JudexisCoreEngine;
import io.judexis.core.check.CheckCategory;
import io.judexis.core.check.movement.HorizontalSpeedCheck;
import io.judexis.core.check.movement.HorizontalSpeedConfig;
import io.judexis.core.check.movement.VerticalIntegrityCheck;
import io.judexis.core.check.movement.VerticalIntegrityConfig;

/**
 * Registers built-in production checks.
 */
public final class CoreCheckBootstrap {
    private CoreCheckBootstrap() {
    }

    public static void registerDefaults(JudexisCoreEngine engine) {
        registerDefaults(engine, new VerticalIntegrityConfig(), new HorizontalSpeedConfig());
    }

    public static void registerDefaults(JudexisCoreEngine engine, VerticalIntegrityConfig config) {
        registerDefaults(engine, config, new HorizontalSpeedConfig());
    }

    public static void registerDefaults(JudexisCoreEngine engine,
                                        VerticalIntegrityConfig verticalConfig,
                                        HorizontalSpeedConfig horizontalConfig) {
        engine.registerCheck(VerticalIntegrityCheck.ID, CheckCategory.MOVEMENT, new VerticalIntegrityCheck(verticalConfig));
        engine.registerCheck(HorizontalSpeedCheck.ID, CheckCategory.MOVEMENT, new HorizontalSpeedCheck(horizontalConfig));
    }
}
