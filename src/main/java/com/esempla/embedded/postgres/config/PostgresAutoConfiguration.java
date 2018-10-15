package com.esempla.embedded.postgres.config;

/**
 * @author Gladîș Vladlen on 7/25/17.
 * @project grm
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.IOException;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "embedded.postgres", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(PostgresProperties.class)
public class PostgresAutoConfiguration {

    private final PostgresProperties properties;

    @Autowired
    public PostgresAutoConfiguration(PostgresProperties properties) {
        this.properties = properties;
    }

    @Configuration
    protected static class EmbeddedPostgresDependencyConfiguration extends DataSourceDependsOnBeanFactoryPostProcessor{
        public EmbeddedPostgresDependencyConfiguration(){
            super("embeddedPostgres");
        }
    }

    @Bean(destroyMethod = "stop")
    @ConditionalOnMissingBean
    public EmbeddedPostgres embeddedPostgres() 
            throws IOException, InterruptedException {        
        
        IRuntimeConfig conf = (properties.getTempPath() != null) 
                ? EmbeddedPostgres.cachedRuntimeConfig( properties.getTempPath().getFile().toPath() )
                : EmbeddedPostgres.defaultRuntimeConfig();
        
        EmbeddedPostgres embeddedPostgres = properties.getDataPath() == null 
                ? new EmbeddedPostgres(properties.getVersion())
                : new EmbeddedPostgres(properties.getVersion(), properties.getDataPath().getFile().getAbsolutePath());

        embeddedPostgres.start(conf,
                properties.getHost(), properties.getPort(),
                properties.getDatabase(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getArguments());
        
        if (properties.getWarmupMs() != null) {
            Thread.sleep(properties.getWarmupMs());
        }
        
        log.info("Embedded pg started {}", embeddedPostgres.getConnectionUrl().get());

        return embeddedPostgres;
    }
}
