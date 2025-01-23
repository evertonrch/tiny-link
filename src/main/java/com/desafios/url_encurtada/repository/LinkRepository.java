package com.desafios.url_encurtada.repository;

import model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, String> {
}
