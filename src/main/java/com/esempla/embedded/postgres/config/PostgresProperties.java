package com.esempla.embedded.postgres.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * @author Gladîș Vladlen on 2/5/18.
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "embedded.postgres")
public class PostgresProperties {

    private String host = "localhost";
    private String username = "postgres";
    private String password = "postgres";
    private String database = "postgres";
    private Resource tempPath;
    private Resource dataPath;
    private Long warmupMs;
    private int port = 5432;
    private Version.Main version = Version.Main.V9_6;    
    private List<String> arguments = Collections.EMPTY_LIST;

}
