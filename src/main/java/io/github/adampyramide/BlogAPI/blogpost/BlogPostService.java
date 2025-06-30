package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.ApiRequestException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.PublicUserDTO;
import io.github.adampyramide.BlogAPI.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository repo;
    private final SecurityUtils securityUtils;

    public BlogPostService(BlogPostRepository repo, SecurityUtils securityUtils) {
        this.repo = repo;
        this.securityUtils = securityUtils;
    }

    public List<BlogPostResponseDTO> getPosts() {
        return repo.findAll().stream()
                .map(blogPostDTO -> {
                    User author = blogPostDTO.getAuthor();
                    return new BlogPostResponseDTO(
                            blogPostDTO.getId(),
                            blogPostDTO.getTitle(),
                            blogPostDTO.getBody(),
                            blogPostDTO.getCreateTime(),
                            new PublicUserDTO(author.getId(),
                                    author.getUsername()
                            )
                    );
                })
                .toList();
    }

    public void createPost(BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();

        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(blogPostDTO.title());
        blogPost.setBody(blogPostDTO.body());
        blogPost.setCreateTime(blogPostDTO.createTime());
        blogPost.setAuthor(user);
        repo.save(blogPost);
    }

    public void editPost(int id, BlogPostRequestDTO blogPostDTO) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        blogPost.setTitle(blogPostDTO.title());
        blogPost.setBody(blogPostDTO.body());
        blogPost.setCreateTime(blogPostDTO.createTime());
        blogPost.setAuthor(user);
        repo.save(blogPost);
    }

    public void deletePost(int id) {
        User user = securityUtils.getAuthenticatedUser();
        BlogPost blogPost = getBlogPostOrThrow(id);
        checkAuthorOrThrow(blogPost, user);

        repo.deleteById(id);
    }

    private BlogPost getBlogPostOrThrow(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiRequestException("Blogpost not found", HttpStatus.NOT_FOUND));
    }

    private void checkAuthorOrThrow(BlogPost blogPost, User user) {
        if (blogPost.getAuthor().getId() != user.getId())
            throw new ApiRequestException("Not authorized", HttpStatus.UNAUTHORIZED);
    }

}
