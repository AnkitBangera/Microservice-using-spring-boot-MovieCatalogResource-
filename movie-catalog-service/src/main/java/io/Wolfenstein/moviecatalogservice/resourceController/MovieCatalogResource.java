package io.Wolfenstein.moviecatalogservice.resourceController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.Wolfenstein.moviecatalogservice.model.CatalogItem;
import io.Wolfenstein.moviecatalogservice.model.Movie;
import io.Wolfenstein.moviecatalogservice.model.Rating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {


	@Autowired 
	RestTemplate restTemplate;
	
	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		
		List<Rating> ratings=Arrays.asList(new Rating("1",4),new Rating("2",4),new Rating("3",4));
		
		
		return ratings.stream().map(rating->{
			Movie movie=restTemplate.getForObject("http://localhost:8081/movie/"+rating.getMovieId(), Movie.class);
				
			return new CatalogItem(movie.getMovieName(), "Action Movie", rating.getRating());
			})
		 
		.collect(Collectors.toList());
	}
}
