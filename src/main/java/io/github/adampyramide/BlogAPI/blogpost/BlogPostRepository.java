package io.github.adampyramide.BlogAPI.blogpost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

    List<BlogPost> findAllByAuthor_Id(int userId);

}
