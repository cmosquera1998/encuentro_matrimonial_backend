package com.encuentro.matrimonial.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name="app.api.swagger.enable", havingValue = "true", matchIfMissing = false)
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {
	
	@Value("${api.swagger.title}")
    private String apiTittle;

    @Value("${api.swagger.description}")
    private String apiDescription;

    @Value("${api.swagger.version}")
    private String apiVersion;

    @Value("${api.swagger.contact.name}")
    private String contactName;

    @Value("${api.swagger.contact.url}")
    private String contactUrl;

    @Value("${api.swagger.contact.email}")
    private String contactEmail;

    @Value("${api.swagger.service.rootpath}")
    private String serviceRootpath;

    @Value("${api.swagger.base-package}")
    private String basePackage;

    @Value("${api.swagger.service.name}")
    private String serviceName;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage(basePackage))
			.paths(PathSelectors.any())
			.build()
			.directModelSubstitute(LocalDate.class, java.sql.Date.class)
			.directModelSubstitute(LocalDateTime.class, java.util.Date.class)
			.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(apiTittle)
			.description(apiDescription)
			.version(apiVersion)
			.contact(new Contact(contactName, contactUrl, contactEmail))
			.build();
	}

	public String getApiTittle() {
		return apiTittle;
	}

	public void setApiTittle(String apiTittle) {
		this.apiTittle = apiTittle;
	}

	public String getApiDescription() {
		return apiDescription;
	}

	public void setApiDescription(String apiDescription) {
		this.apiDescription = apiDescription;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactUrl() {
		return contactUrl;
	}

	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getServiceRootpath() {
		return serviceRootpath;
	}

	public void setServiceRootpath(String serviceRootpath) {
		this.serviceRootpath = serviceRootpath;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}