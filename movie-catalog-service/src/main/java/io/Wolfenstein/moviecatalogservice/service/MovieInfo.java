package io.Wolfenstein.moviecatalogservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.Wolfenstein.moviecatalogservice.model.CatalogItem;
import io.Wolfenstein.moviecatalogservice.model.Movie;
import io.Wolfenstein.moviecatalogservice.model.Rating;

@Service
public class MovieInfo {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpEntity<Movie> entity;
	
	@HystrixCommand(fallbackMethod="getFallbackCatalogItem",
			commandProperties= {
					@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="2000"),
					@HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="5"),
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="50"),
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="500")
/*
			threadPoolKey="movieInfoPool",//this will create separate space or threadPool by the name "movieInfoPool"
			threadPoolProperties= {
					@HystrixProperty(name="coreSize",value="20"),//core size indicate number of request that can consume thread and wait for response
					@HystrixProperty(name="maxQueueSize",value="10")//maxQueueSize indicate how many more requests can wait which 
					//have not consumed any resource and wait for resource
*/					
			})
	public CatalogItem getCatalogItem(@PathVariable Rating rating) {
		Movie movie= restTemplate.postForObject("http://movie-info-service/movies/" + rating.getMovieId(), entity,
				Movie.class);
		return new CatalogItem(movie.getMovieId(), movie.getDescription(), rating.getRating());
		
	}
	public CatalogItem getFallbackCatalogItem(@PathVariable Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
}
