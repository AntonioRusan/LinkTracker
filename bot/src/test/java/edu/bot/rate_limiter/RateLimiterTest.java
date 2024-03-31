package edu.bot.rate_limiter;

import edu.java.bot.BotApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {BotApplication.class})
@AutoConfigureMockMvc
public class RateLimiterTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRateLimiter() throws Exception {
        String json = """
            {
              "id": 0,
              "url": "test.com",
              "description": "test",
              "tgChatIds": [
                0
              ]
            }
            """;

        for (int i = 0; i < 64; i++) {
            mockMvc.perform(post("/api/updates")
                    .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
        }

        mockMvc.perform(post("/api/updates")
                .contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isTooManyRequests());

    }
}
