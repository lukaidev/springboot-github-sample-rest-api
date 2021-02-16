package com.sample.github.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.github.api.model.GithubPublicRepo;
import com.sample.github.api.model.GithubRepoCommit;
import com.sample.github.api.model.RepoAnalytics;
import com.sample.github.api.service.GithubService;

@RestController
@RequestMapping("/github")
public class RepositoriesController {
	
	private final GithubService githubService;
	 

	public RepositoriesController(GithubService githubService) {
		this.githubService = githubService;
	}


	@GetMapping("/repositories")
	public List<GithubPublicRepo> getPublicRepos(
			@RequestParam(name = "since", required = true) Long since) {
		return githubService.getPublicRepos(since);
	}
	
	@GetMapping("repos/{owner}/{repo}/analytics")
	public RepoAnalytics getRepoAnalytics(
			@PathVariable(required = true) String owner,
			@PathVariable(required = true) String repo) {
		return githubService.getAnalytics(owner, repo);
	}
	
	@GetMapping("repos/{owner}/{repo}/commits")
	public List<GithubRepoCommit> getRepoCommits(
			@PathVariable(required = true) String owner,
			@PathVariable(required = true) String repo) {
		return githubService.getCommits(owner, repo);
	}
	
	
}
