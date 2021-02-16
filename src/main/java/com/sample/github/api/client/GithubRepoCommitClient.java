package com.sample.github.api.client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.github.api.model.GithubRepoCommit;

@Service
public class GithubRepoCommitClient {
	
	private static final  String  OWNER_STRING = "{owner}";
	private static final  String  REPO_STRING = "{repo}";
	
	@Value("${github.public-repo-commits-url}")
	private String githubPublicRepoCommitsUrl;
	
	private final RestTemplate restTemplate;
	
	public GithubRepoCommitClient(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	public List<GithubRepoCommit> getGithubRepoCommits(String owner, String repo) {
		String actualUrl = Optional.ofNullable(githubPublicRepoCommitsUrl)
				.map(r -> r.replace(OWNER_STRING, owner))
				.map(r -> r.replace(REPO_STRING, repo))
				.orElseThrow(() -> new RuntimeException("Invalid github.public-repo-commits-url"));
		
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(actualUrl);
		
		List<?> commits = restTemplate
				.getForObject(
						uri.toUriString(),
						List.class);
		
		return commits.stream()
				.map(r -> mapCommit((Map<?, ?>) r))
				.collect(Collectors.toList());
	}
	
	private GithubRepoCommit mapCommit(Map<?, ?> value) {
		Map<?, ?> commit = getCommit(value);
		Map<?, ?> author = getAuthor(value);
		Map<?, ?> commiter = getCommiter(value);
		String login = ObjectUtils.isEmpty(commiter) ? StringUtils.EMPTY : (String) commiter.get("login");
		String avatar = ObjectUtils.isEmpty(commiter) ? StringUtils.EMPTY : (String) commiter.get("avatar_url");
		return GithubRepoCommit.builder()
				.name((String) author.get("name"))
				.email((String) author.get("email"))
				.date((String) author.get("date"))
				.commitMessage((String) commit.get("message"))
				.login(login)
				.avatar(avatar)
				.build();
	}
	
	private Map<?, ?> getCommit(Map<?, ?> value) {
		return (LinkedHashMap<?, ?>) value.get("commit");
	}
	
	private Map<?, ?> getAuthor(Map<?, ?> value) {
		Map<?, ?> commit = getCommit(value);
		return (LinkedHashMap<?, ?>) commit.get("author");
	}
	
	private Map<?, ?> getCommiter(Map<?, ?> value) {
		return (LinkedHashMap<?, ?>) value.get("committer");
	}

}
