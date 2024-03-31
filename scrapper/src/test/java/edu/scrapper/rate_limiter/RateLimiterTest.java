package edu.scrapper.rate_limiter;

import edu.java.ScrapperApplication;
import edu.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ScrapperApplication.class})
@AutoConfigureMockMvc
public class RateLimiterTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRateLimiter() throws Exception {
        mockMvc.perform(post("/api/tg-chat/1"))
            .andExpect(status().isOk());

        for (int i = 0; i < 59; i++) {
            mockMvc.perform(get("/api/links").header("Tg-Chat-Id", 1))
                .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/links").header("Tg-Chat-Id", 1))
            .andExpect(status().isTooManyRequests());
    }
}
