
package com.ewers.alarmclock.weatherunderground;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class WeatherUnderground {

	private Response response;
	private Forecast forecast;
	private CurrentObservation current_observation;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public CurrentObservation getCurrentObservation() {
		return this.current_observation;
	}

	public void setCurrentObservation(CurrentObservation currentObservation) {
		this.current_observation = currentObservation;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
