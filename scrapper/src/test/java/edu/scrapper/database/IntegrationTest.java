package edu.scrapper.database;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;
    public static KafkaContainer KAFKA;
    private final static Logger LOGGER = LogManager.getLogger();

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);

        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));
        KAFKA.start();
    }

    private static void runMigrations(JdbcDatabaseContainer<?> dbContainer) {
        try (Connection connection = DriverManager.getConnection(
            dbContainer.getJdbcUrl(),
            dbContainer.getUsername(),
            dbContainer.getPassword()
        )) {
            Path changelogPath = Paths.get("").toAbsolutePath().getParent().resolve("migrations");
            Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase
                .Liquibase("master.xml", new DirectoryResourceAccessor(changelogPath), database);
            updateDatabase(liquibase);
        } catch (SQLException | LiquibaseException | FileNotFoundException ex) {
            LOGGER.error("Error while doing migrations: " + ex.getMessage());
        }
    }

    private static void updateDatabase(Liquibase liquibase) {
        try {
            new CommandScope(UpdateCommandStep.COMMAND_NAME)
                .addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, liquibase.getDatabase())
                .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, liquibase.getChangeLogFile())
                .addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog())
                .addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS, liquibase.getChangeLogParameters())
                .execute();
        } catch (LiquibaseException ex) {
            System.out.println("Error while updating: " + ex.getMessage());
        }
    }

    @DynamicPropertySource
    static void jdbcAndKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("kafka.bootstrapServers", KAFKA::getBootstrapServers);
    }

}
