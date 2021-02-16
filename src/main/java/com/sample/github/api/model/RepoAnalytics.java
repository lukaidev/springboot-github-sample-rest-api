package com.sample.github.api.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RepoAnalytics {

	private Integer totalCommits;
	private List<CommiterInfo> commitersInfo;
	private List<GithubRepoCommit> commits;
	
}
