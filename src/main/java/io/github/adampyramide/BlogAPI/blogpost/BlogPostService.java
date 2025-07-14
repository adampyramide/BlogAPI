package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<BlogPostResponseDTO> getBlogPosts() {
        return repo.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public BlogPostResponseDTO getBlogPostById(Long id) {
        return mapper.toResponseDTO(
                repo.findById(id)
                        .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND))
        );
    }

    public void createBlogPost(BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();

        BlogPost blogPost = mapper.toEntity(blogPostDTO);
        blogPost.setAuthor(user);
        blogPost.setCreateTime(LocalDateTime.now());
        repo.save(blogPost);
    }

    public void editBlogPostById(Long id, BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        mapper.updateEntityWithDto(blogPostDTO, blogPost);
        repo.save(blogPost);
    }

    public void deleteBlogPostById(Long id) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        repo.deleteById(id);
    }

    public void bulkDeletePostsByIds(List<Long> ids) {
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

    public List<BlogPostResponseDTO> getBlogPostsByUserId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);

        return repo.findAllByAuthor_Id(userId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    private BlogPost getBlogPostOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND));
    }

    private void checkAuthorOrThrow(BlogPost blogPost, User user) {
        if (blogPost.getAuthor().getId() != user.getId())
            throw new CustomException("Post not created by user", HttpStatus.UNAUTHORIZED);
    }

}
