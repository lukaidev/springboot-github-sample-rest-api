package com.sample.github.api.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sample.github.api.client.GithubPublicRepoClient;
import com.sample.github.api.client.GithubRepoCommitClient;
import com.sample.github.api.model.CommiterInfo;
import com.sample.github.api.model.GithubPublicRepo;
import com.sample.github.api.model.GithubRepoCommit;
import com.sample.github.api.model.RepoAnalytics;

import lombok.Builder;
import lombok.Data;

@Service
public class GithubService {
	
	private final GithubPublicRepoClient publicRepo;
	private final GithubRepoCommitClient repoCommit;
	
	public GithubService(GithubPublicRepoClient publicRepo, GithubRepoCommitClient repoCommit) {
		this.publicRepo = publicRepo;
		this.repoCommit = repoCommit;
	}

	public List<GithubPublicRepo> getPublicRepos(Long since) {
		return publicRepo.getGithubRepos(since);
	}

	public List<GithubRepoCommit> getCommits(String owner, String repo) {
		return repoCommit.getGithubRepoCommits(owner, repo);
	}


	public RepoAnalytics getAnalytics(String owner, String repo) {
		List<GithubRepoCommit> commits = repoCommit.getGithubRepoCommits(owner, repo);
		return RepoAnalytics.builder()
				.totalCommits(commits.size())
				.commitersInfo(getCommiterInfo(commits))
				.commits(commits)
				.build();
	}

	private List<CommiterInfo> getCommiterInfo(List<GithubRepoCommit> commits) {
		
		Map<Object, Long> infos = commits.stream()
				.collect(Collectors.groupingBy(m -> InfoMap.builder()
						.avatar(m.getAvatar())
						.login(m.getLogin())
						.email(m.getEmail())
						.build(), 
						Collectors.counting()));
		
		
		return infos.entrySet().stream()
				.map(k -> CommiterInfo.builder()
						.login(getInfoMapValue(k.getKey(), "login"))
						.email(getInfoMapValue(k.getKey(), "email"))
						.avatar(getInfoMapValue(k.getKey(), "avatar"))
						.commitCount(k.getValue())
						.build())
				.collect(Collectors.toList());
	}
	
	private String getInfoMapValue(Object obj, String field) {
		InfoMap infoMap = (InfoMap) obj;
		String value = StringUtils.EMPTY;
		switch (field) {
		case "login":
			value = infoMap.getLogin();
			break;
		case "email":
			value = infoMap.getEmail();
			break;
		case "avatar":
			value = infoMap.getAvatar();
			break;
		default:
			throw new RuntimeException("Invalid field");
		}
		return value;
	}
	

}

@Data
@Builder(toBuilder = true)
class InfoMap {
	
	private String login;
	private String email;
	private String avatar;
	
}
