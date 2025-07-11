package io.github.adampyramide.BlogAPI.blogpost;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Page<BlogPost> findAll(PageableDefault pageable);

    List<BlogPost> findAllByAuthor_Id(long userId);

}
