package org.egov.access;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Import(TracerConfiguration.class)
public class EgovAccesscontrolApplication {

	private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	public static void main(String[] args) {
		SpringApplication.run(EgovAccesscontrolApplication.class, args);
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
		mapper.setTimeZone(TimeZone.getTimeZone(timeZone));
		converter.setObjectMapper(mapper);
		return converter;
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
		return objectMapper;
	}

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
				configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
			}

		};
	}
}
