package edu.scrapper.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class PostgresTest extends IntegrationTest {
    @Test
    @DisplayName("Проверка, что таблицы из миграции были добавлены")
    void checkTablesExistTest() {
        try (
            Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
            )
        ) {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tablesResultSet = dbm.getTables(null, null, null, null);
            List<String> allTables = new ArrayList<>();
            List<String> neededTables = List.of("link", "chat", "chat_link");
            while (tablesResultSet.next()) {
                allTables.add(tablesResultSet.getString("TABLE_NAME"));
            }
            assertTrue(allTables.containsAll(neededTables));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
