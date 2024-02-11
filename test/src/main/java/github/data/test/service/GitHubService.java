package github.data.test.service;

import github.data.test.model.Branch;
import github.data.test.model.RepositoryFromGitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
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
            RepositoryFromGitHub[] repositories = restTemplate.getForObject(url, RepositoryFromGitHub[].class);
            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .peek(repo -> {
                        if (!repo.isFork()) {
                            setBranchesForRepository(username, repo);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            log.error("Error fetching repositories from GitHub for user {}: {}", username, e.getMessage());
            return Collections.emptyList();
        }
    }

    private void setBranchesForRepository(String username, RepositoryFromGitHub repository) {
        String branchesUrl = String.format("%s/repos/%s/%s/branches", githubApiUrl, username, repository.getName());
        try {
            Branch[] branches = restTemplate.getForObject(branchesUrl, Branch[].class);
            // Ustaw tylko nazwę brancha i ostatni SHA commita, ignorując inne szczegóły
            List<Branch> processedBranches = Arrays.stream(branches)
                    .map(branch -> new Branch(branch.getName(), branch.getLastCommitSha()))
                    .collect(Collectors.toList());
            repository.setBranches(processedBranches);
        } catch (RestClientException e) {
            log.error("Error fetching branches for repository {}: {}", repository.getName(), e.getMessage());
        }
    }
}

