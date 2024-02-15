package github.data.test.data;
import github.data.test.model.RepositoryFromGitHub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

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

        webTestClient.get()
                .uri("/api/users/" + username + "/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryFromGitHub.class)
                .consumeWith(response -> {
                    List<RepositoryFromGitHub> repositories = response.getResponseBody();
                    assertNotNull(repositories, "The list of repositories should not be null");
                    assertTrue(repositories.stream().noneMatch(RepositoryFromGitHub::isFork), "The list should only contain repositories that are not forks");
                });
    }
    @Test
    public void whenUserNotFound_thenThrowException() {
        String nonExistentUser = "Sampeteqk";

        webTestClient.get()
                .uri("/api/users/" + nonExistentUser + "/repos")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("User '" + nonExistentUser + "' not found on GitHub");
    }

    @Test
    public void whenUserHasNoRepositories_thenReturnEmptyList() {
        String userWithNoRepos = "Sampeteq";

        webTestClient.get()
                .uri("/api/users/" + userWithNoRepos + "/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryFromGitHub.class)
                .hasSize(0);
    }

    @Test
    public void whenUserHasForkAndNonForkRepositories_thenReturnOnlyNonFork() {
        String userWithForkAndNonForkRepos = "Sampeteq";

        webTestClient.get()
                .uri("/api/users/" + userWithForkAndNonForkRepos + "/repos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryFromGitHub.class)
                .consumeWith(response -> {
                    List<RepositoryFromGitHub> repositories = response.getResponseBody();
                    assertNotNull(repositories, "The list of repositories should not be null");
                    assertTrue(repositories.stream().noneMatch(RepositoryFromGitHub::isFork),
                            "The list should only contain repositories that are not forks");
                });
    }

}
