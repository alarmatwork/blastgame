package com.fitness.blast.integrations.wiki.dto;

import java.util.Map;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikipediaApiResponseDto {
	
	private String batchcomplete;
	private Query query;
	
	@Data
	public class Query {
		private Map<String, Object> pages;
	};
	
}
