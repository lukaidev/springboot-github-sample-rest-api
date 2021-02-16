package com.sample.github.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class GithubRepoCommit {

	private String name;
	private String email;
	private String date;
	private String commitMessage;
	private String login;
	private String avatar;
	
}
