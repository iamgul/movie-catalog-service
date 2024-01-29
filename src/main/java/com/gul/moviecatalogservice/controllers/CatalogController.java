package com.gul.moviecatalogservice.controllers;

import com.gul.moviecatalogservice.models.MovieCatalog;
import com.gul.moviecatalogservice.models.MovieInfo;
import com.gul.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{userId}")
    public List<MovieCatalog> getCatalog(@PathVariable String userId) {
        UserRating response = restTemplate.getForObject("http://localhost:8082/ratings/" + userId, UserRating.class);
        return response.getRatings().stream().map(rating -> {
            MovieInfo movieInfoResponse = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), MovieInfo.class);
            return new MovieCatalog(movieInfoResponse.getName(), movieInfoResponse.getDesc(), rating.getRating());
        }).toList();

    }


}
/*
MovieInfo movieInfoResponse =   webClientBuilder.build()
        .get()
        .uri("http://localhost:8081/movies/" + rating.getMovieId())
        .retrieve()
        .bodyToMono(MovieInfo.class)
        .block();
        */
