package com.sample.github.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CommiterInfo {
	
	private String login;
	private String email;
	private String avatar;
	private Long commitCount;
	

}
