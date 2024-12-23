package com.ust.retail.store;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication 
@EnableJpaAuditing
@EnableWebMvc
@EnableAsync
@Configuration
@EnableScheduling
public class PimAPI extends SpringBootServletInitializer implements WebMvcConfigurer {

	private static final String DEFAULT_TIME_ZONE = "America/New_York";

	@Autowired
	private ObjectMapper objectMapper;

	public static void main(String[] args) {
		SpringApplication.run(PimAPI.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PimAPI.class);
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(mvcTaskExecutor());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		WebMvcConfigurer.super.configureMessageConverters(converters);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		objectMapper.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
		converters.stream()
				.filter(MappingJackson2HttpMessageConverter.class::isInstance)
				.map(MappingJackson2HttpMessageConverter.class::cast)
				.forEach(c -> c.setObjectMapper(objectMapper));
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
	}

	@Bean
	public ThreadPoolTaskExecutor mvcTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setThreadNamePrefix("mvc-task-");
		return taskExecutor;
	}

	@Bean
	@Primary
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	@Primary
	public ITemplateResolver templateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

		templateResolver.setPrefix("emailTemplates/");

		templateResolver.setSuffix(".html");

		templateResolver.setTemplateMode(TemplateMode.HTML);

		return templateResolver;

	}

	@Bean
	@Primary
	public TemplateEngine templateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(this.templateResolver());
		return templateEngine;
	}

	@Bean
	public TemplateEngine pdfTemplateEngine() {
		TemplateEngine pdfTemplateEngine = new SpringTemplateEngine();
		ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
		pdfTemplateResolver.setPrefix("pdfTemplates/");
		pdfTemplateResolver.setSuffix(".html");
		pdfTemplateResolver.setTemplateMode(TemplateMode.HTML);

		pdfTemplateEngine.setTemplateResolver(pdfTemplateResolver);
		return pdfTemplateEngine;
	}

	@Bean
	public SimpleModule emptyStringAsNullModule() {
		SimpleModule module = new SimpleModule();

		module.addDeserializer(
				String.class,
				new StdDeserializer<>(String.class) {

					private static final long serialVersionUID = 1L;

					@Override
					public String deserialize(JsonParser parser, DeserializationContext context)
							throws IOException {
						String result = StringDeserializer.instance.deserialize(parser, context);
						if (StringUtils.isEmpty(result)) {
							return null;
						}
						return result;
					}
				});
		return module;
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
