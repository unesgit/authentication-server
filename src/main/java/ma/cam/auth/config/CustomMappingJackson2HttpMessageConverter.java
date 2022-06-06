package ma.cam.auth.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CustomMappingJackson2HttpMessageConverter {

	@Bean
	public MappingJackson2HttpMessageConverter jacksonMapping() {
		return new MappingJackson2HttpMessageConverter(objectMapper());
	}

	/**
	 * used to fix the timezone to default not at GMT
	 * 
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper() {
		final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
		return mapper;
	}

}
