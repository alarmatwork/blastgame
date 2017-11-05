package com.fitness.blast.integrations;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fitness.blast.integrations.wiki.dto.WikipediaApiResponseDto;
import com.fitness.blast.integrations.wiki.dto.WikiResponseDto;

@Data
@Slf4j
@Component
public class WikipediaApi {
	
	// example URL
	// /w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7C
	//	pageterms&generator=geosearch&colimit=30&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description%7Clabel&ggscoord=58.3776%7C26.7290&ggsradius=1000&ggslimit=50
	private static String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7Cpageterms&generator=geosearch&colimit=30&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description%7Clabel&ggscoord={lat}%7C{lon}&ggsradius=1000&ggslimit=50";

	@Autowired
	private RestTemplate restTemplate;
	

	public List<WikiResponseDto> getNearbyPOIs(Double lat, Double lon) {

		String targetURL = WIKIPEDIA_API_URL.replace("{lat}", lat.toString());
		targetURL = targetURL.replace("{lon}", lon.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			
			log.info("Wiki URL: " + targetURL);
			ResponseEntity<WikipediaApiResponseDto> response = this.restTemplate.exchange(new URI(targetURL), HttpMethod.GET, entity, WikipediaApiResponseDto.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				
				log.info("Wiki response: " + response.getBody().toString());
				if (response.getBody().getQuery() != null && response.getBody().getQuery().getPages() != null) {

					List<WikiResponseDto> results = new ArrayList<WikiResponseDto>();
					for (String key : response.getBody().getQuery().getPages().keySet()) {

						Map<String, Object> page = (Map<String, Object>) response.getBody().getQuery().getPages().get(key);
						List<Object> coordinates = (List<Object>) page.get("coordinates");
						Map<String, Object> terms = (Map<String, Object>) page.get("terms");

						WikiResponseDto wikiResponseObject = new WikiResponseDto();
						wikiResponseObject.setTitle((String) page.get("title"));

						if (coordinates != null && coordinates.size() > 0) {
							Map<String, Object> coord = (Map<String, Object>) coordinates.get(0);
							wikiResponseObject.setLatitude((coord.get("lat")) + "");
							wikiResponseObject.setLongitude((coord.get("lon")) + "");
						}

						if (terms != null) {
							List<Object> termDescriptions = (List<Object>) terms.get("description");
							if (termDescriptions != null && termDescriptions.size() > 0) {
								wikiResponseObject.setDescription((String) termDescriptions.get(0));
							}
						}

						results.add(wikiResponseObject);
					}
					
					return results;
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}
}
