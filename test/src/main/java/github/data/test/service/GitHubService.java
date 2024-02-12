package github.data.test.service;

import github.data.test.exception.UserNotFoundException;
import github.data.test.model.Branch;
import github.data.test.model.RepositoryFromGitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private final String githubApiUrl = "https://api.github.com";
    private static final Logger log = LoggerFactory.getLogger(GitHubService.class);

    @Autowired
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
}

