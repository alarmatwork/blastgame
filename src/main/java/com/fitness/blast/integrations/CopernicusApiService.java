package com.fitness.blast.integrations;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fitness.blast.integrations.copernicus.dto.CopernicusApiResponseDto;

@Service
@Slf4j
public class CopernicusApiService {

	@Autowired
	private RestTemplate restTemplate;

	public final String API_URL = "http://ramani.ujuizi.com/ddl/wms?TIME=2009-01-01T00%3A00%3A00.000Z&token=2cda05da150497b1864fea8e38a91d15&package=com.alarmatwork.ramaniapi&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo&SRS=EPSG%3A4326&BBOX={lon1}%2C{lat1}%2C{lon2}%2C{lat2}&X=100&Y=100&INFO_FORMAT=text%2Fjson&QUERY_LAYERS=simS3seriesCoverGlobal%2Fcoverclass&WIDTH=200&HEIGHT=200";

	static Map<Integer, String> values = new HashMap<>();

	static {

		values.put(11, "Post-flooding or irrigated croplands (or aquatic)");
		values.put(14, "Rainfed croplands");
		values.put(20, "Mosaic cropland (50-70%) / vegetation (grassland/shrubland/forest) (20-50%)");
		values.put(30, "Mosaic vegetation (grassland/shrubland/forest) (50-70%) / cropland (20-50%)");
		values.put(40, "Closed to open (>15%) broadleaved evergreen or semi-deciduous forest (>5m)");
		values.put(50, "Closed (>40%) broadleaved deciduous forest (>5m)");
		values.put(60, "Open (15-40%) broadleaved deciduous forest/woodland (>5m)");
		values.put(70, "Closed (>40%) needleleaved evergreen forest (>5m)");
		values.put(90, "Open (15-40%) needleleaved deciduous or evergreen forest (>5m)");
		values.put(100, "Closed to open (>15%) mixed broadleaved and needleleaved forest (>5m)");
		values.put(110, "Mosaic forest or shrubland (50-70%) / grassland (20-50%)");
		values.put(120, "Mosaic grassland (50-70%) / forest or shrubland (20-50%)");
		values.put(-126, "Closed to open (>15%) (broadleaved or needleleaved, evergreen or deciduous) shrubland (<5m)");
		values.put(-116, "Closed to open (>15%) herbaceous vegetation (grassland, savannas or lichens/mosses)");
		values.put(-106, "Sparse (<15%) vegetation");
		values.put(-96, "Closed to open (>15%) broadleaved forest regularly flooded (semi-permanently or temporarily) - Fresh or brackish water");
		values.put(-86, "Closed (>40%) broadleaved forest or shrubland permanently flooded - Saline or brackish water");
		values.put(-76, "Closed to open (>15%) grassland or woody vegetation on regularly flooded or waterlogged soil - Fresh, brackish or saline water");
		values.put(-66, "Artificial surfaces and associated areas (Urban areas >50%)");
		values.put(-56, "Bare areas");
		values.put(-46, "Water bodies");
		values.put(-36, "Permanent snow and ice");
		values.put(-26, "No data (burnt areas, clouds,…)");

	}


	public String getUrbanizationLayer(double lat, double lon) {

		String stringResponse = "";
		String targetURL = API_URL;
		targetURL = targetURL.replace("{lat1}", roundify(lat - 0.001, 6));
		targetURL = targetURL.replace("{lat2}", roundify(lat + 0.001, 6));

		targetURL = targetURL.replace("{lon1}", roundify(lon - 0.001, 6));
		targetURL = targetURL.replace("{lon2}", roundify(lon + 0.001, 6));

		log.info("URL: " + targetURL);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {

			ResponseEntity<CopernicusApiResponseDto> response = this.restTemplate.exchange(new URI(targetURL), HttpMethod.GET, entity,
					CopernicusApiResponseDto.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				log.info(response.getBody().toString());

// TODO: better IF conditioning
				Double apiValue = response.getBody().getFeatures().get(0).getFeatureInfo().get(0).getValue();
				int intApiValue = apiValue.intValue();

				return this.getHumanReadableValue(intApiValue);
			}

		} catch (Exception e) {
			log.error("Execption", e);
		}
		return stringResponse;

	}


	public static String roundify(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor + "";
	}


	public String getHumanReadableValue(Integer key) {

		if (values.containsKey(key)) {
			return values.get(key);
		}

		return "";
	}

}
