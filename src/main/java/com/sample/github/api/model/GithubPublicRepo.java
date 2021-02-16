package com.sample.github.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class GithubPublicRepo {

	private Integer id;
	private String name;
	private String fullName;
	private String description;
	private String owner;
	private String avatar;
	
}
