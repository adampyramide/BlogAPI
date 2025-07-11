package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BlogPostService {

    private final BlogPostRepository repo;
    private final BlogPostMapper mapper;

    private final UserRepository userRepository;

    private final SecurityUtils securityUtils;

    public BlogPostService(BlogPostRepository repo, BlogPostMapper blogPostMapper, UserRepository userRepository, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = blogPostMapper;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    public List<BlogPostResponseDTO> getPosts() {
        return repo.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public BlogPostResponseDTO getPostById(long id) {
        return mapper.toResponseDTO(
                repo.findById(id)
                        .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND))
        );
    }

    public void createPost(BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();

        BlogPost blogPost = mapper.toEntity(blogPostDTO);
        blogPost.setAuthor(user);
        blogPost.setCreateTime(LocalDateTime.now());
        repo.save(blogPost);
    }

    public void editPost(long id, BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        mapper.updateBlogPostFromDto(blogPostDTO, blogPost);
        repo.save(blogPost);
    }

    public void deletePost(long id) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        repo.deleteById(id);
    }

    public void bulkDeletePosts(List<Long> ids) {
        User user = securityUtils.getAuthenticatedUser();

        List<BlogPost> posts = repo.findAllById(ids);

        if (posts.size() != ids.size()) {
            throw new CustomException("Zero posts found", HttpStatus.NOT_FOUND);
        }

        for (BlogPost post : posts) {
            checkAuthorOrThrow(post, user);
        }

        repo.deleteAll(posts);
    }

    public List<BlogPostResponseDTO> getPostsByUserId(long userId) {
        if (!userRepository.existsById(userId))
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);

        return repo.findAllByAuthor_Id(userId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    private BlogPost getBlogPostOrThrow(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND));
    }

    private void checkAuthorOrThrow(BlogPost blogPost, User user) {
        if (blogPost.getAuthor().getId() != user.getId())
            throw new CustomException("Not authorized", HttpStatus.UNAUTHORIZED);
    }

}
