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
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;

import de.flapdoodle.embed.process.config.IRuntimeConfig;

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
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        
        IRuntimeConfig conf = (properties.getTempPath() != null) 
                ? EmbeddedPostgres.cachedRuntimeConfig( Paths.get(properties.getTempPath().getURI()) )
                : EmbeddedPostgres.defaultRuntimeConfig();
        
        EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres(
                properties.getVersion());
        

        embeddedPostgres.start(conf,
                properties.getHost(), properties.getPort(),
                properties.getDatabase(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getArguments());

        return embeddedPostgres;
    }
}
