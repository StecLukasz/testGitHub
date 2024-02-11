package github.data.test.controller;

import github.data.test.model.RepositoryFromGitHub;
import github.data.test.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GitHubController {
    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}/repos")
    public List<RepositoryFromGitHub> getUserRepositories(@PathVariable String username) {
        return gitHubService.getNonForkRepositoriesForUser(username);
    }
}
