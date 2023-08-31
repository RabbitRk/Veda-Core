package com.veda.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.veda.entity.Greeting;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface GreetingRepository extends PagingAndSortingRepository<Greeting, UUID>{
     
}
