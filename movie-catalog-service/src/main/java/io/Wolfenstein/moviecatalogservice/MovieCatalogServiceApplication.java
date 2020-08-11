package io.Wolfenstein.moviecatalogservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.Wolfenstein.moviecatalogservice.model.Movie;

@SpringBootApplication
@EnableEurekaClient//enable eureka 
@EnableCircuitBreaker//enable hystrix
@EnableHystrixDashboard//enable Hystrix Dashboard that gives ability to display data in dashboard
public class MovieCatalogServiceApplication {
	
	@Autowired
	private HttpHeaders headers;
	
	@Bean
	public HttpEntity<Movie> getHttpEntity() {
		//HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMWJiY2RhYzFjYzY1YjExYWQ3NzY5OTJjMjNkODRlYyIsInN1YiI6IjVmMmU5OGVlMGMwYjM4MDAzMmRlMGU4OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.OH7xmsIn4Y2bSWPxp1jAXtqG1tG5-z5EN9qMwE1Esg8";
		headers.set("Authorization", "Bearer "+accessToken);

		return new HttpEntity<Movie>(headers);
	}
	
	@Bean
	public WebClient.Builder getWebClient(){
		return WebClient.builder();
	}
	@Bean
	public HttpHeaders getHeader() {
		return new HttpHeaders();
	}
	
	@Bean
	@LoadBalanced//it does service discovery in a load balanced way i.e without providing actual link we are providing application name and still it works
	public RestTemplate getRestTemplet() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
