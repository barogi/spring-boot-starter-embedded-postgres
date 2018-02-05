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
import org.springframework.core.annotation.Order;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(prefix = "embedded.postgres", name = "create-context")
@EnableConfigurationProperties(PostgresProperties.class)
public class PostgresAutoConfiguration {

    @Autowired
    private PostgresProperties properties;

    @Bean
    @Order(1)
    @ConditionalOnMissingBean
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres(properties.getVersion());

        embeddedPostgres.start(properties.getHost(), properties.getPort(), properties.getDatabaseName(),
                properties.getUsername(), properties.getPassword());

        return embeddedPostgres;
    }

}
