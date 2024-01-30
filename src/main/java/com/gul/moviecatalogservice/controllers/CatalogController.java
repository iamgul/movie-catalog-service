package com.gul.moviecatalogservice.controllers;

import com.gul.moviecatalogservice.models.MovieCatalog;
import com.gul.moviecatalogservice.models.MovieInfo;
import com.gul.moviecatalogservice.models.UserRating;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientbuilder;

    @GetMapping("/{userId}")
    public List<MovieCatalog> getCatalog(@PathVariable String userId) {
        //If we want to get list of services at runtime
        List<String> services = discoveryClient.getServices();
        System.err.println(services);
        List<ServiceInstance> instances = discoveryClient.getInstances("movie-info-service");
        instances.forEach(i->{
            System.out.println(i.getHost());
            System.out.println(i.getInstanceId());
            System.out.println(i.getMetadata());
            System.out.println(i.getPort());
            System.out.println(i.getServiceId());
            System.out.println(i.getUri());
            System.out.println(i.getScheme());
        });


        UserRating response = restTemplate.getForObject("http://movie-rating-service/ratings/" + userId, UserRating.class);
        return response.getRatings().stream().map(rating -> {

/* Reactive way of Handling, Since RestTemplate is about to Deprecate
            MovieInfo movieInfoResponse = webClientbuilder.build()
                    .get()
                    .uri("http://movie-info-service/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(MovieInfo.class)
                    .block();*/

            MovieInfo movieInfoResponse = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), MovieInfo.class);
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
