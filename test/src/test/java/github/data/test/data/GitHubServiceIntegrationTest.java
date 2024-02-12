package github.data.test.data;
import github.data.test.model.RepositoryFromGitHub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GitHubServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetNonForkRepositoriesForUser() {
        String username = "Sampeteq";

        webTestClient.get().uri("/api/users/" + username + "/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryFromGitHub.class)
                .consumeWith(response -> {
                    List<RepositoryFromGitHub> repositories = response.getResponseBody();
                    assertNotNull(repositories, "The list of repositories should not be null");
                    assertTrue(repositories.stream().noneMatch(RepositoryFromGitHub::isFork), "The list should only contain repositories that are not forks");
                });
    }
}
