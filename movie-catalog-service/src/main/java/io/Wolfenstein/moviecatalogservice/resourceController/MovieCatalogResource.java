package io.Wolfenstein.moviecatalogservice.resourceController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	private RestTemplate restTemplate;
	
	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		
		UserRating userRatings=restTemplate.getForObject("http://localhost:8082/rating/user/"+userId, UserRating.class);
		
		
		return userRatings.getRating().stream().map(rating->{
			Movie movie=restTemplate.getForObject("http://localhost:8081/movie/"+rating.getMovieId(), Movie.class);
				
			/*Movie movie=webClientBuilder.build()//using builder pattern ang giving us the client
			.get()//type of method get,post,put................
			.uri("http://localhost:8081/movie/"+rating.getMovieId())//which url to call
			.retrieve()//go do the fetch
			.bodyToMono(Movie.class)//As its a asynchronous method bodyToMono() promises to give us the movie object but cant expect the time what it will give
			.block();//it block the flow as its a async method bodyToMono() doesn't return. 
*/			
			
			return new CatalogItem(movie.getMovieName(), "Action Movie", rating.getRating());
			})
		 
		.collect(Collectors.toList());
	}
}
