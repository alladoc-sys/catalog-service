package com.alfred.moviecatalogservice.resources;

import com.alfred.moviecatalogservice.models.CatalogItem;
import com.alfred.moviecatalogservice.models.Movie;
import com.alfred.moviecatalogservice.models.Rating;
import com.alfred.moviecatalogservice.models.UserRating;
import com.alfred.moviecatalogservice.services.MovieInfo;
import com.alfred.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // get rating
        UserRating ratings = userRatingInfo.getUserRating(userId);

        return ratings.getUserRating().stream().map(rating -> {
            // For each movie ID, call movie info service
            return movieInfo.getCatalogItem(rating);
        })
                .collect(Collectors.toList());

    }





}

/*

        public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

            return Collections.singletonList(
                new CatalogItem( "Transformers",  "Test" ,  4 )
        );*/

        /*  List<Rating> ratings = Arrays.asList(
                new Rating("004", 7),
                new Rating("005", 2),
                new Rating("000", 0)
        );*/

        /* Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/