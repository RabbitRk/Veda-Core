package com.veda.repository.master;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.veda.entity.master.Category;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface CategoryRepository extends PagingAndSortingRepository<Category, UUID>{
    
}
