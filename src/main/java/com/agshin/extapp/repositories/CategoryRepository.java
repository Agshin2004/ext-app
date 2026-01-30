package com.agshin.extapp.repositories;

import com.agshin.extapp.model.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Boolean> existsByCategoryNameAndUser_Id(String categoryName, Long userId);

    // same thing but with jpql
    @Query("""
                    SELECT category 
                    FROM Category category
                    WHERE category.id = :id
                    AND category.user.id = :userId
            """)
    Optional<Category> findByIdAndUser_Id(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    Page<Category> findByUser_Id(Long id, Pageable pageable);
}
