package edu.scrapper.database.jooq;

import edu.java.ScrapperApplication;
import edu.java.models.Link;
import edu.java.repositories.jooq.JooqLinkRepository;
import edu.scrapper.database.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    private static final String TEST_URL = "test.com";

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link link = Link.create(TEST_URL);
        Long addedId = jooqLinkRepository.add(link);
        assertTrue(addedId > 0);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        Link link = Link.create(TEST_URL);
        Long addedId = jooqLinkRepository.add(link);
        Optional<Link> linkFromDb = jooqLinkRepository.findById(addedId);
        assertTrue(linkFromDb.isPresent());
        assertThat(linkFromDb.get().url()).isEqualTo(TEST_URL);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        Link link = Link.create(TEST_URL);
        Long addedId = jooqLinkRepository.add(link);
        Integer deleted = jooqLinkRepository.delete(addedId);
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Link link = Link.create(TEST_URL);
        Long addedId1 = jooqLinkRepository.add(link);
        link = Link.create("test2.com");
        Long addedId2 = jooqLinkRepository.add(link);
        List<Link> linksFromDb = jooqLinkRepository.findAll();
        assertThat(linksFromDb.size()).isEqualTo(2);
    }

}
