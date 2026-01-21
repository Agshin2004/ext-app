package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Boolean> existsByCategoryName(String categoryName);
}
