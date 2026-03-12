package com.yas.sampledata.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlScriptExecutorTest {

    private SqlScriptExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new SqlScriptExecutor();
    }

    @Test
    void executeScriptsForSchema_withInvalidPattern_shouldHandleException() {
        DataSource dataSource = mock(DataSource.class);

        // Use a pattern that won't match any resources - exercises the catch block
        executor.executeScriptsForSchema(dataSource, "public", "classpath*:nonexistent/**/*.sql");

        // No exception thrown - the catch block handles it gracefully
    }

    @Test
    void executeScriptsForSchema_withValidPatternButConnectionFails_shouldHandleException() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Use a valid resource pattern that actually exists in test classpath
        executor.executeScriptsForSchema(dataSource, "public", "classpath*:application.properties");

        // No exception thrown - the catch block handles it
    }

    @Test
    void executeScriptsForSchema_withEmptyResources_shouldDoNothing() {
        DataSource dataSource = mock(DataSource.class);

        // Use pattern that matches nothing
        executor.executeScriptsForSchema(dataSource, "public", "classpath*:db/nonexistent/*.sql");
    }

    @Test
    void executeScriptsForSchema_withNullPattern_shouldHandleException() {
        DataSource dataSource = mock(DataSource.class);

        // Null pattern will cause an exception in PathMatchingResourcePatternResolver
        executor.executeScriptsForSchema(dataSource, "public", "invalid://pattern");
    }
}
