package com.resumeforge.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration.
 *
 * Provides a custom migration strategy that runs repair() before migrate().
 * This is required when a previous failed migration (e.g. from an old
 * ddl-auto:create run) left a FAILED row in flyway_schema_history —
 * Flyway refuses to migrate until that row is cleared.
 */
@Slf4j
@Configuration
public class FlywayConfig {

    /**
     * Repair then migrate strategy.
     *
     * repair() removes any FAILED migration entries from flyway_schema_history
     * and recalculates checksums, then migrate() applies pending migrations.
     */
    @Bean
    public FlywayMigrationStrategy repairThenMigrate() {
        return (Flyway flyway) -> {
            log.info("Flyway: running repair() to clear any failed migration markers...");
            flyway.repair();
            log.info("Flyway: repair complete, running migrate()...");
            flyway.migrate();
            log.info("Flyway: migration complete.");
        };
    }
}
