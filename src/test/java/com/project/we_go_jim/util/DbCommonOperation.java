package com.project.we_go_jim.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class DbCommonOperation {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbCommonOperation(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void initializeTestData() {
        executeSqlScript("db/insert-data-script.sql");
    }

    @Transactional
    public void cleanUp() {
        executeSqlScript("db/delete-data-script.sql");
    }

    private void executeSqlScript(String scriptPath) {
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String script = reader.lines().collect(Collectors.joining("\n"));
            jdbcTemplate.execute(script);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL script from classpath: " + scriptPath, e);
        }
    }
}
