package com.fitness.blast.integrations.copernicus.dto;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CopernicusApiResponseDto {

	private String type;
	private List<Feature> features;

	@Data
	public class Feature {
		private Geometry geometry;
		private Properties properties;
	};

	@Data
	public class Geometry {
		private String type;
	};

	@Data
	public class Properties {

		private Integer iIndex;
		private Integer jIndex;

		private Double gridCentreLon;
		private Double gridCentreLat;
	};
}
