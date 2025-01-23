package com.desafios.encurtador_url.repository;

import com.desafios.encurtador_url.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {

    Optional<Link> findByUrlEncurtada(String urlEncurtada);
}
