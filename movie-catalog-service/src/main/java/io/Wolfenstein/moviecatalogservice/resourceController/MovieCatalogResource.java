package io.Wolfenstein.moviecatalogservice.resourceController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.Wolfenstein.moviecatalogservice.model.CatalogItem;
import io.Wolfenstein.moviecatalogservice.model.Movie;
import io.Wolfenstein.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private DiscoveryClient discoveryClient;//can return multiple  port number , multiple  service id list of same instance
	
	@Autowired 
	private RestTemplate restTemplate;
	
	@Autowired
	private HttpEntity<Movie> entity;
	
	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		
		UserRating userRatings=restTemplate.getForObject("http://rating-data-service/rating/user/"+userId, UserRating.class);
	/*	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMWJiY2RhYzFjYzY1YjExYWQ3NzY5OTJjMjNkODRlYyIsInN1YiI6IjVmMmU5OGVlMGMwYjM4MDAzMmRlMGU4OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.OH7xmsIn4Y2bSWPxp1jAXtqG1tG5-z5EN9qMwE1Esg8";
		headers.set("Authorization", "Bearer "+accessToken);

		HttpEntity<Movie> entity = new HttpEntity<Movie>(headers);*/
		
		
		return userRatings.getRating().stream().map(rating->{
		//	Movie movie=restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
			Movie movie = restTemplate.postForObject("http://movie-info-service/movies/"+rating.getMovieId(), entity, Movie.class);
			/*Movie movie=webClientBuilder.build()//using builder pattern ang giving us the client
			.get()//type of method get,post,put................
			.uri("http://localhost:8081/movie/"+rating.getMovieId())//which url to call
			.retrieve()//go do the fetch
			.bodyToMono(Movie.class)//As its a asynchronous method bodyToMono() promises to give us the movie object but cant expect the time what it will give
			.block();//it block the flow as its a async method bodyToMono() doesn't return. 
*/			
			
			return new CatalogItem(movie.getMovieId(), movie.getDescription(), rating.getRating());
			})
		 
		.collect(Collectors.toList());
	}
}
