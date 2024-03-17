package edu.scrapper;

import edu.java.ScrapperApplication;
import edu.java.models.Link;
import edu.java.repositories.JdbcLinkRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JdbcTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    private static final String TEST_URL = "test.com";

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link link = Link.create(TEST_URL);
        Integer updated = jdbcLinkRepository.add(link);
        assertThat(updated).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {

        Link link = Link.create(TEST_URL);
        Integer updated = jdbcLinkRepository.add(link);
        assertThat(updated).isEqualTo(1);

        Optional<Link> linFromDb = jdbcLinkRepository.findById(2L);
        assertTrue(linFromDb.isPresent());
        assertThat(linFromDb.get().url()).isEqualTo(TEST_URL);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Link link = Link.create(TEST_URL);
        Integer updated = jdbcLinkRepository.add(link);
        Integer deleted = jdbcLinkRepository.delete(3L);
        assertThat(deleted).isEqualTo(1);
    }
}
