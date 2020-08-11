package io.Wolfenstein.moviecatalogservice.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.Wolfenstein.moviecatalogservice.model.Rating;
import io.Wolfenstein.moviecatalogservice.model.UserRating;

@Service
public class UserRatingInfo {
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallbackUserRatingInfo",
			commandProperties= {
					@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="2000"),
					@HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="6"),
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="50"),//suppose 3 of last 6 requestVolumeThreshold fails then activate circuit breaker
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="500")
					/*
					threadPoolKey="ratingInfoPool",//this will create separate space or threadPool by the name "ratingInfoPool"
					threadPoolProperties= {
							@HystrixProperty(name="coreSize",value="20"),//core size indicate number of request that can consume thread and wait for response
							@HystrixProperty(name="maxQueueSize",value="10")//maxQueueSize indicate how many more requests can wait which 
							//have not consumed any resource and wait for resource
*/					
			})
	public UserRating getUserRatingInfo(@PathVariable String userId) {
		return restTemplate.getForObject("http://rating-data-service/rating/user/" + userId,
				UserRating.class);
	}

	public UserRating getFallbackUserRatingInfo(@PathVariable String userId) {
		UserRating userRating =new UserRating();
		userRating.setRating(Arrays.asList(new Rating("0", 0)));
		return userRating;
	}
}
