package com.gul.moviecatalogservice.controllers;

import com.gul.moviecatalogservice.models.MovieCatalog;
import com.gul.moviecatalogservice.models.MovieInfo;
import com.gul.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userId}")
    public List<MovieCatalog> getCatalog(@PathVariable String userId) {

        List<Rating> ratings = Arrays.asList(
                new Rating("1234", "Rating 10"),
                new Rating("5678", "Rating 9"));

        return ratings.stream().map(rating -> {
            MovieInfo movieInfoResponse = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), MovieInfo.class);
            return new MovieCatalog(movieInfoResponse.getName(), movieInfoResponse.getDesc(), rating.getRating());
        }).collect(Collectors.toList());

    }

}
