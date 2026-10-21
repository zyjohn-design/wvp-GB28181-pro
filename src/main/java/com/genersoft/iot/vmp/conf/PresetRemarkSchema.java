package com.genersoft.iot.vmp.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/** Creates the independent platform remark table once, including on existing installations. */
@Component
public class PresetRemarkSchema implements InitializingBean {
    private final DataSource dataSource;

    public PresetRemarkSchema(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private boolean exists() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String schema;
            try {
                schema = connection.getSchema();
            } catch (SQLFeatureNotSupportedException | AbstractMethodError exception) {
                schema = null;
            }
            try (ResultSet tables = connection.getMetaData().getTables(connection.getCatalog(), schema, null, new String[]{"TABLE"})) {
                while (tables.next()) {
                    if ("wvp_preset_remark".equalsIgnoreCase(tables.getString("TABLE_NAME"))) return true;
                }
                return false;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws SQLException {
        if (exists()) return;
        try {
            new ResourceDatabasePopulator(new ClassPathResource("db/preset-remark.sql")).execute(dataSource);
        } catch (RuntimeException exception) {
            // Another node sharing this database may have created the table concurrently.
            if (!exists()) throw exception;
        }
    }
}
