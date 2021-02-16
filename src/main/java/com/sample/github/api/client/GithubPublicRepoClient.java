package com.sample.github.api.client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.github.api.model.GithubPublicRepo;

@Service
public class GithubPublicRepoClient {
	
	@Value("${github.public-repo-url}")
	private String githubPublicRepoUrl;
	
	private final RestTemplate restTemplate;

	public GithubPublicRepoClient(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}
	
	public List<GithubPublicRepo> getGithubRepos(Long since) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(githubPublicRepoUrl);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("since", since.toString());
		
		uri.queryParams(params);    
		
		List<?> owners = restTemplate
				.getForObject(
						uri.toUriString(),
						List.class);
		
		return owners.stream()
				.map(r -> mapRepo((Map<?, ?>) r))
				.collect(Collectors.toList());
	}
	
	private GithubPublicRepo mapRepo(Map<?, ?> value) {
		Map<?,?> ownerInfo = getOwerInfo(value);
		return GithubPublicRepo.builder()
				.id((Integer) value.get("id"))
				.name((String) value.get("name"))
				.fullName((String) value.get("full_name"))
				.description((String) value.get("description"))
				.owner((String) ownerInfo.get("login"))
				.avatar((String) ownerInfo.get("avatar_url"))
				.build();
	}
	
	private Map<?, ?> getOwerInfo(Map<?, ?> value) {
		return (LinkedHashMap<?, ?>) value.get("owner");
	}

}
