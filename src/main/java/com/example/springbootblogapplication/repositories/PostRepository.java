package com.example.springbootblogapplication.repositories;

import com.example.springbootblogapplication.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN p.account a WHERE LOWER(p.content) LIKE %:query% OR LOWER(a.firstName) LIKE %:query% OR LOWER(a.lastName) LIKE %:query%")
    List<Post> search(@Param("query") String query);

    @Query("SELECT p FROM Post p WHERE p.account.id = :accountId")
    List<Post> findByAccountId(@Param("accountId") Long accountId);
}

