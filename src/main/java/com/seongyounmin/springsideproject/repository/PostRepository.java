package com.seongyounmin.springsideproject.repository;

import com.seongyounmin.springsideproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
