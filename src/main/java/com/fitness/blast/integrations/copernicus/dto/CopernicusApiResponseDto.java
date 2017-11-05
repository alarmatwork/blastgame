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
	static public class Feature {
		private Geometry geometry;
		private Properties properties;
		private List<FeatureInfo> featureInfo;
	};

	@Data
	static public class Geometry {
		private String type;
	};

	@Data
	static public class Properties {

		private Integer iIndex;
		private Integer jIndex;

		private Double gridCentreLon;
		private Double gridCentreLat;
	};
	
	@Data
	static public class FeatureInfo {
		private String time;
		private Double value;
	};
}
