package com.alfred.moviecatalogservice.services;

import com.alfred.moviecatalogservice.models.CatalogItem;
import com.alfred.moviecatalogservice.models.Movie;
import com.alfred.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            commandProperties= {
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="5"),
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="60"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000")
            })
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(),  "desc" ,  rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found.",  "No Movie found" ,  rating.getRating());
    }

}
