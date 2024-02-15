package github.data.test.service;

import github.data.test.exception.UserNotFoundException;
import github.data.test.model.Branch;
import github.data.test.model.RepositoryFromGitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private final String githubApiUrl = "https://api.github.com";
    private static final Logger log = LoggerFactory.getLogger(GitHubService.class);

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryFromGitHub> getNonForkRepositoriesForUser(String username) {
        String url = githubApiUrl + "/users/" + username + "/repos";
        try {
            ResponseEntity<RepositoryFromGitHub[]> response = restTemplate.getForEntity(url, RepositoryFromGitHub[].class);

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User '" + username + "' not found on GitHub");
            }

            RepositoryFromGitHub[] repositories = response.getBody();
            for (RepositoryFromGitHub repo : repositories) {
                if (!repo.isFork()) {
                    String branchesUrl = String.format("%s/repos/%s/%s/branches", githubApiUrl, username, repo.getName());
                    Branch[] branches = restTemplate.getForObject(branchesUrl, Branch[].class);
                    repo.setBranches(Arrays.asList(branches));
                }
            }
            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            log.error("Error fetching repositories from GitHub for user {}: {}", username, e.getMessage());
            throw new UserNotFoundException("User '" + username + "' could not be found or accessed on GitHub");
        }
    }

    // ciągle mam błąd z org.reactivestreams.publisher

//    private final WebClient webClient;
//    private final String githubApiUrl = "https://api.github.com";
//    private static final Logger log = LoggerFactory.getLogger(GitHubService.class);
//
//    public GitHubService(WebClient webClient) {
//        this.webClient = webClient;
//    }
//
//    public List<RepositoryFromGitHub> getNonForkRepositoriesForUser(String username) {
//        String url = "/users/" + username + "/repos";
//
//        List<RepositoryFromGitHub> repositories = webClient.get().uri(url)
//                .retrieve()
//                .bodyToFlux(RepositoryFromGitHub.class)
//                .filter(repo -> !repo.isFork())
//                .flatMap(repo -> getBranchesForRepo(username, repo).collectList()
//                        .map(branches -> {
//                            repo.setBranches(branches);
//                            return repo;
//                        }))
//                .collectList()
//                .block();
//
//        return repositories;
//    }
//
//    private Flux<Branch> getBranchesForRepo(String username, RepositoryFromGitHub repo) {
//        String branchesUrl = String.format("/repos/%s/%s/branches", username, repo.getName());
//
//        return webClient.get().uri(branchesUrl)
//                .retrieve()
//                .bodyToFlux(Branch.class)
//                .onErrorResume(e -> {
//                    log.error("Problem with fetching branches for repository {}", repo.getName(), e);
//                    return Flux.empty();
//                });
//    }
}

